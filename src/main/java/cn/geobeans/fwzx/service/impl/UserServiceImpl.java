//package cn.geobeans.fwzx.service.impl;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.stereotype.Service;
//
//import cn.geobeans.fwzx.model.Resource;
//import cn.geobeans.fwzx.model.Role;
//import cn.geobeans.fwzx.model.User;
//import cn.geobeans.fwzx.service.UserService;
//import cn.geobeans.fwzx.util.StringUtil;
//
///**
// *@author liuxi
// *@parameter E-mail:15895982509@163.com
// *@version 创建时间:2016-5-21下午4:20:37
// */
//@Service
//public class UserServiceImpl implements UserService {
//
//	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
//
//	@javax.annotation.Resource(name = "jdbcTemplate")
//	private JdbcTemplate jdbcTemplate;
//
//	@Override
//	public int insert(final User model) {
//		String sql = "INSERT INTO FWZX_USER(ID,ACCOUNT,PASSWORD,NICKNAME,REGISTERTIME,ACCOUNTSTATE,TELEPHONE,EMAIL) VALUES(?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
//		int result = -1;
//		try {
//			if (getUserByAccount(model.getAccount()) == null) {
//				result = jdbcTemplate.update(sql,
//						new PreparedStatementSetter() {
//							@Override
//							public void setValues(PreparedStatement ps)
//									throws SQLException {
//								ps.setString(1, model.getId());
//								ps.setString(2, model.getAccount());
//								ps.setString(3, model.getPassword());
//								ps.setString(4, model.getNickname());
//								ps.setString(5, model.getRegisterTime());
//								ps.setString(6, model.getAccountState());
//								ps.setString(7, model.getTelephone());
//								ps.setString(8, model.getEmail());
//							}
//						});
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return result;
//	}
//
//	@Override
//	public int delete(final String id) {
//		String sql = "DELETE FROM FWZX_USER WHERE ID=?";
//		int result = -1;
//		try {
//			result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//				@Override
//				public void setValues(PreparedStatement preparedstatement) throws SQLException {
//					preparedstatement.setString(1, id);
//				}
//			});
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return result;
//	}
//
//	@Override
//	public User get(String id) {
//		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE ID=?";
//		User model = null;
//		try {
//			model = jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<User>(User.class));
//
//		} catch (Exception e) {
//			return null;
//		}
//		return model;
//	}
//
//	@Override
//	public int update(final User model) {
//		String sql = "UPDATE FWZX_USER SET ACCOUNT=?,PASSWORD=?,NICKNAME=?,REGISTERTIME=TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE=?,TELEPHONE=?,EMAIL=? WHERE ID=?";
//		int result = -1;
//		try {
//			result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//				@Override
//				public void setValues(PreparedStatement ps) throws SQLException {
//					ps.setString(1, model.getAccount());
//					ps.setString(2, model.getPassword());
//					ps.setString(3, model.getNickname());
//					ps.setString(4, model.getRegisterTime());
//					ps.setString(5, model.getAccountState());
//					ps.setString(6, model.getTelephone());
//					ps.setString(7, model.getEmail());
//					ps.setString(8, model.getId());
//				}
//			});
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return result;
//	}
//
//	@Override
//	public List<User> findList() {
//		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER";
//		List<User> list = null;
//		try {
//			list = (List<User>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return list;
//	}
//
//	@Override
//	public User getUserByAccount(String account){
//		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE ACCOUNT=?";
//		User model = null;
//		try {
//			model = jdbcTemplate.queryForObject(sql, new Object[] { account }, new BeanPropertyRowMapper<User>(User.class));
//		} catch (Exception e) {
//			return null;
//		}
//		return model;
//
//
//	}
//
//	@Override
//	public List<Role> getRoleListByUserId(String userId) {
//		String sql = "SELECT R.ID,R.NAME,R.DESCRIPTION FROM FWZX_ROLE R LEFT JOIN FWZX_USER_ROLE U_R ON R.ID=U_R.ROLE_ID WHERE U_R.USER_ID=?";
//		List<Role> list = null;
//		try {
//			list = jdbcTemplate.query(sql,new Object[]{ userId }, new BeanPropertyRowMapper<Role>(Role.class));
//		} catch (Exception e) {
//			return null;
//		}
//		return list;
//	}
//
//	@Override
//	public List<Resource> getResourceById(String userId) {
//		String sql = "SELECT ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION " +
//				"FROM FWZX_RESOURCE " +
//				"WHERE ID IN" +
//				"(SELECT R_RE.RESOURCE_ID FROM(SELECT R.ID as ID FROM FWZX_USER_ROLE U_R LEFT JOIN FWZX_ROLE R ON U_R.ROLE_ID = R.ID WHERE U_R.USER_ID=?) R_ID " +
//				"		LEFT JOIN FWZX_ROLE_RESOURCE R_RE ON R_RE.ROLE_ID=R_ID.ID)";
//		List<Resource> list = null;
//		try {
//			list = jdbcTemplate.query(sql,new Object[]{ userId }, new BeanPropertyRowMapper<Resource>(Resource.class));
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return list;
//	}
//
//	@Override
//	public List<User> getUsersByParams(String account, String nickname) {
//		String sql = "SELECT ID,ACCOUNT,PASSWORD,NICKNAME,TO_CHAR(REGISTERTIME,'yyyy-mm-dd hh24:mi:ss'),ACCOUNTSTATE,TELEPHONE,EMAIL FROM FWZX_USER WHERE 1=1";
//		List<User> list = null;
//		if(!StringUtil.isNull(account)){
//			sql = sql + " AND ACCOUNT='"+account+"'";
//		}
//		if(!StringUtil.isNull(nickname)){
//			sql = sql + " AND NICKNAME LIKE'%"+nickname+"%'";
//		}
//		try{
//			list = (List<User>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
//		}catch(Exception e){
//			return null;
//		}
//		return list;
//	}
//
//}
