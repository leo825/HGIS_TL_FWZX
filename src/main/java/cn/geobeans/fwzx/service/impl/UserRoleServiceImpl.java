package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.model.UserModel;
import cn.geobeans.fwzx.service.UserRoleService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-6-2下午6:50:27
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private static Logger logger = Logger.getLogger(UserRoleServiceImpl.class);
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final UserModel user, final RoleModel role) {

        String sql = "INSERT INTO FWZX_USER_ROLE(USER_ID,ROLE_ID) VALUES(?,?)";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, user.getId());
                    ps.setString(2, role.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    @Override
    public int delete(final UserModel user, final RoleModel role) {
        String sql = "DELETE FROM FWZX_USER_ROLE WHERE USER_ID=? AND ROLE_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedstatement) throws SQLException {
                    preparedstatement.setString(1, user.getId());
                    preparedstatement.setString(2, role.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int update(final UserModel oldUser, final RoleModel oldRole, final UserModel newUser, final RoleModel newRole) {
        String sql = "UPDATE FWZX_USER_ROLE SET USER_ID=?,ROLE_ID=? WHERE USER_ID=? AND ROLE_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, newUser.getId());
                    ps.setString(2, newRole.getId());
                    ps.setString(3, oldUser.getId());
                    ps.setString(4, oldRole.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public List<RoleModel> getRoleListByUserId(String userId) {
        String sql = "SELECT R.ID,R.NAME,R.DESCRIPTION FROM (SELECT ROLE_ID FROM FWZX_USER_ROLE WHERE USER_ID='" + userId + "') U_R INNER JOIN FWZX_ROLE R ON U_R.ROLE_ID=R.ID";
        List<RoleModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public List<UserModel> getUserListByRoleId(String roleId) {
        String sql = "SELECT U.ID,U.ACCOUNT,U.PASSWORD,U.NICKNAME,U.REGISTERTIME,U.ACCOUNTSTATE,U.TELEPHONE,U.EMAIL FROM (SELECT USER_ID FROM FWZX_USER_ROLE WHERE ROLE_ID='" + roleId + "') U_R INNER JOIN FWZX_USER U ON U_R.USER_ID=U.ID";
        List<UserModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserModel>(UserModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public boolean isUserRoleExist(UserModel user, RoleModel role) {
        String sql = "SELECT USER_ID,ROLE_ID FROM FWZX_USER_ROLE WHERE USER_ID='" + user.getId() + "'" + " AND ROLE_ID='" + role.getId() + "'";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql);
            if (!StringUtil.isListEmpty(list)) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    @Override
    public int deleteAllUserRoleByUserId(final String userId) {
        String sql = "DELETE FROM FWZX_USER_ROLE WHERE USER_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedstatement) throws SQLException {
                    preparedstatement.setString(1, userId);
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

}
