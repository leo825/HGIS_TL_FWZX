//package cn.geobeans.fwzx.service.impl;
//
//import cn.geobeans.fwzx.model.Role;
//import cn.geobeans.fwzx.model.User;
//import cn.geobeans.fwzx.service.UserRoleService;
//import cn.geobeans.fwzx.util.StringUtil;
//import org.apache.log4j.Logger;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author liuxi
// * @version 创建时间:2016-6-2下午6:50:27
// * @parameter E-mail:15895982509@163.com
// */
//@Service
//public class UserRoleServiceImpl implements UserRoleService {
//
//    private static Logger logger = Logger.getLogger(UserRoleServiceImpl.class);
//    @Resource(name = "jdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public int insert(final User user, final Role role) {
//
//        String sql = "INSERT INTO FWZX_USER_ROLE(USER_ID,ROLE_ID) VALUES(?,?)";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, user.getId());
//                    ps.setString(2, role.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//
//    }
//
//    @Override
//    public int delete(final User user, final Role role) {
//        String sql = "DELETE FROM FWZX_USER_ROLE WHERE USER_ID=? AND ROLE_ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement preparedstatement) throws SQLException {
//                    preparedstatement.setString(1, user.getId());
//                    preparedstatement.setString(2, role.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public int update(final User oldUser, final Role oldRole, final User newUser, final Role newRole) {
//        String sql = "UPDATE FWZX_USER_ROLE SET USER_ID=?,ROLE_ID=? WHERE USER_ID=? AND ROLE_ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, newUser.getId());
//                    ps.setString(2, newRole.getId());
//                    ps.setString(3, oldUser.getId());
//                    ps.setString(4, oldRole.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public List<Role> getRoleListByUserId(String userId) {
//        String sql = "SELECT R.ID,R.NAME,R.DESCRIPTION FROM (SELECT ROLE_ID FROM FWZX_USER_ROLE WHERE USER_ID='" + userId + "') U_R INNER JOIN FWZX_ROLE R ON U_R.ROLE_ID=R.ID";
//        List<Role> list = null;
//        try {
//            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//    @Override
//    public List<User> getUserListByRoleId(String roleId) {
//        String sql = "SELECT U.ID,U.ACCOUNT,U.PASSWORD,U.NICKNAME,U.REGISTERTIME,U.ACCOUNTSTATE,U.TELEPHONE,U.EMAIL FROM (SELECT USER_ID FROM FWZX_USER_ROLE WHERE ROLE_ID='" + roleId + "') U_R INNER JOIN FWZX_USER U ON U_R.USER_ID=U.ID";
//        List<User> list = null;
//        try {
//            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//    @Override
//    public boolean isUserRoleExist(User user, Role role) {
//        String sql = "SELECT USER_ID,ROLE_ID FROM FWZX_USER_ROLE WHERE USER_ID='" + user.getId() + "'" + " AND ROLE_ID='" + role.getId() + "'";
//        List<Map<String, Object>> list = null;
//        try {
//            list = jdbcTemplate.queryForList(sql);
//            if (!StringUtil.isListEmpty(list)) {
//                return true;
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return false;
//    }
//
//    @Override
//    public int deleteAllUserRoleByUserId(final String userId) {
//        String sql = "DELETE FROM FWZX_USER_ROLE WHERE USER_ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement preparedstatement) throws SQLException {
//                    preparedstatement.setString(1, userId);
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//}
