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
//import cn.geobeans.fwzx.service.ResourceService;
//
///**
// * @author liuxi
// * @version 创建时间:2016-5-22下午9:09:07
// * @parameter E-mail:15895982509@163.com
// */
//@Service
//public class ResourceServiceImpl implements ResourceService {
//
//    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
//
//    @javax.annotation.Resource(name = "jdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public int insert(final Resource model) {
//        String sql = "INSERT INTO FWZX_RESOURCE (ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION) VALUES(?,?,?,?,?)";
//        int result = -1;
//        try {
//            if (getResourceByName(model.getName()) == null) {
//                result = jdbcTemplate.update(sql,
//                        new PreparedStatementSetter() {
//                            @Override
//                            public void setValues(PreparedStatement ps)
//                                    throws SQLException {
//                                ps.setString(1, model.getId());
//                                ps.setString(2, model.getParentId());
//                                ps.setString(3, model.getName());
//                                ps.setString(4, model.getNickname());
//                                ps.setString(5, model.getDescription());
//                            }
//                        });
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public int delete(final String id) {
//        String sql = "DELETE FROM FWZX_RESOURCE WHERE ID=?";
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
//    public Resource get(String id) {
//        String sql = "SELECT ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION FROM FWZX_RESOURCE WHERE ID=?";
//        Resource model = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<Resource>(Resource.class));
//
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return model;
//    }
//
//    @Override
//    public int update(final Resource model) {
//        String sql = "UPDATE FWZX_RESOURCE SET PARENT_ID=?,NAME=?,NICKNAME=?,DESCRIPTION=? WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, model.getParentId());
//                    ps.setString(2, model.getName());
//                    ps.setString(3, model.getNickname());
//                    ps.setString(4, model.getDescription());
//                    ps.setString(5, model.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    @Override
//    public List<Resource> findList() {
//        String sql = "SELECT ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION FROM FWZX_RESOURCE";
//        List<Resource> list = null;
//        try {
//            list = (List<Resource>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<Resource>(Resource.class));
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return list;
//    }
//
//    @Override
//    public Resource getResourceByName(String resourceName) {
//        String sql = "SELECT ID,PARENT_ID,NAME,NICKNAME,DESCRIPTION FROM FWZX_RESOURCE WHERE NAME=?";
//        Resource model = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{resourceName}, new BeanPropertyRowMapper<Resource>(Resource.class));
//
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return model;
//
//    }
//
//}
