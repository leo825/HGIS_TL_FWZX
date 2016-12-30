//package cn.geobeans.fwzx.service.impl;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
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
//import cn.geobeans.fwzx.service.RoleService;
//import cn.geobeans.fwzx.util.StringUtil;
//
///**
// * @author liuxi
// * @version 创建时间:2016-5-22下午4:35:33
// * @parameter E-mail:15895982509@163.com
// */
//@Service
//public class RoleServiceImpl implements RoleService {
//
//    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
//
//    @javax.annotation.Resource(name = "jdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public int insert(final Role model) {
//        String sql = "INSERT INTO FWZX_ROLE(ID,NAME,DESCRIPTION) VALUES(?,?,?)";
//        int result = -1;
//        try {
//            if (getRoleByName(model.getName()) == null) {
//                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps) throws SQLException {
//                        ps.setString(1, model.getId());
//                        ps.setString(2, model.getName());
//                        ps.setString(3, model.getDescription());
//                    }
//                });
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public int delete(final String id) {
//        String sql = "DELETE FROM FWZX_ROLE WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement preparedstatement) throws SQLException {
//                    preparedstatement.setString(1, id);
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public Role get(String id) {
//        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE ID=?";
//        Role model = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<Role>(Role.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return model;
//    }
//
//    @Override
//    public int update(final Role model) {
//        String sql = "UPDATE FWZX_ROLE SET NAME=?,DESCRIPTION=? WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, model.getName());
//                    ps.setString(2, model.getDescription());
//                    ps.setString(3, model.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public List<Role> findList() {
//        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE";
//        List<Role> list = null;
//        try {
//            list = (List<Role>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//    @Override
//    public Role getRoleByName(String roleName) {
//        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE NAME=?";
//        Role model = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{roleName}, new BeanPropertyRowMapper<Role>(Role.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return model;
//    }
//
//    @Override
//    public List<Resource> getResourceByRoleId(String roleId) {
//        String sql = "SELECT RES.ID,RES.PARENT_ID,RES.NAME,RES.NICKNAME,RES.DESCRIPTION FROM (SELECT RESOURCE_ID FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=?) R_R INNER JOIN FWZX_RESOURCE RES ON R_R.RESOURCE_ID=RES.ID";
//        List<Resource> list = null;
//        try {
//            list = jdbcTemplate.query(sql, new Object[]{roleId}, new BeanPropertyRowMapper<Resource>(Resource.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//    @Override
//    public List<Role> getListByNameAndDescription(String name, String description) {
//        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE 1=1";
//        List<Role> list = new ArrayList<>();
//        try {
//            if (!StringUtil.isNull(name)) {
//                sql = sql + " AND NAME="+name;
//            }
//            if (!StringUtil.isNull(description)) {
//                sql = sql + " AND DESCRIPTION LIKE '%"+description+"%'";
//            }
//            list = (List<Role>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//}
