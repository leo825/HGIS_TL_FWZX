package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UserModel;
import cn.geobeans.fwzx.service.UserService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-21下午4:20:37
 */
@Service
public class UserServiceImpl implements UserService {
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insert(final UserModel model) {
		String sql = "INSERT INTO FWZX_USER(ID,ACCOUNT,PASSWORD,NICKNAME,REGISTERTIME,ACCOUNTSTATE,TELEPHONE,EMAIL) VALUES(?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
		int result = -1;
		try {
			if (getUserByAccount(model.getAccount()) == null) {
				result = jdbcTemplate.update(sql,
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setString(1, model.getId());
								ps.setString(2, model.getAccount());
								ps.setString(3, model.getPassword());
								ps.setString(4, model.getNickname());
								ps.setString(5, model.getRegisterTime());
								ps.setString(6, model.getAccountState());
								ps.setString(7, model.getTelephone());
								ps.setString(8, model.getEmail());
							}
						});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	public int delete(final String id) {
		String sql = "DELETE FROM FWZX_USER WHERE ID=?";
		int result = -1;
		try {
			result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedstatement) throws SQLException {
					preparedstatement.setString(1, id);
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	public UserModel get(String id) {
		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE ID=?";
		UserModel model = null;
		try {
			model = jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<UserModel>(UserModel.class));

		} catch (Exception e) {
			return null;
		}
		return model;
	}

	@Override
	public int update(final UserModel model) {
		String sql = "UPDATE FWZX_USER SET ACCOUNT=?,PASSWORD=?,NICKNAME=?,REGISTERTIME=TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE=?,TELEPHONE=?,EMAIL=? WHERE ID=?";
		int result = -1;
		try {
			result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, model.getAccount());
					ps.setString(2, model.getPassword());
					ps.setString(3, model.getNickname());
					ps.setString(4, model.getRegisterTime());
					ps.setString(5, model.getAccountState());
					ps.setString(6, model.getTelephone());
					ps.setString(7, model.getEmail());
					ps.setString(8, model.getId());
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	public List<UserModel> findList() {
		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER";
		List<UserModel> list = null;
		try {
			list = (List<UserModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserModel>(UserModel.class));
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	@Override
	public UserModel getUserByAccount(String account){
		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE ACCOUNT=?";
		UserModel model = null;
		try {
			model = jdbcTemplate.queryForObject(sql, new Object[] { account }, new BeanPropertyRowMapper<UserModel>(UserModel.class));
		} catch (Exception e) {
			return null;
		}
		return model;
	
		
	}

	@Override
	public List<RoleModel> getRoleListByUserId(String userId) {
		String sql = "SELECT R.ID,R.NAME,R.DESCRIPTION FROM FWZX_ROLE R LEFT JOIN FWZX_USER_ROLE U_R ON R.ID=U_R.ROLE_ID WHERE U_R.USER_ID=?";
		List<RoleModel> list = null;
		try {
			list = jdbcTemplate.query(sql,new Object[]{ userId }, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	@Override
	public List<ResourceModel> getResourceById(String userId) {
		String sql = "SELECT ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION " +
				"FROM FWZX_RESOURCE " +
				"WHERE ID IN" +
				"(SELECT R_RE.RESOURCE_ID FROM(SELECT R.ID as ID FROM FWZX_USER_ROLE U_R LEFT JOIN FWZX_ROLE R ON U_R.ROLE_ID = R.ID WHERE U_R.USER_ID=?) R_ID " +
				"		LEFT JOIN FWZX_ROLE_RESOURCE R_RE ON R_RE.ROLE_ID=R_ID.ID)";
		List<ResourceModel> list = null;
		try {
			list = jdbcTemplate.query(sql,new Object[]{ userId }, new BeanPropertyRowMapper<ResourceModel>(ResourceModel.class));
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	@Override
	public List<UserModel> getUsersByParams(String account, String nickname) {
		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE 1=1";
		List<UserModel> list = null;
		if(!StringUtil.isNull(account)){
			sql = sql + " AND ACCOUNT='"+account+"'";
		}
		if(!StringUtil.isNull(nickname)){
			sql = sql + " AND NICKNAME LIKE'%"+nickname+"%'";
		}
		try{
			list = (List<UserModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserModel>(UserModel.class));
		}catch(Exception e){
			return null;
		}
		return list;
	}

}
