//package cn.geobeans.fwzx.service.impl;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.stereotype.Service;
//
//import cn.geobeans.fwzx.model.Project;
//import cn.geobeans.fwzx.model.Resource;
//import cn.geobeans.fwzx.model.Usage;
//import cn.geobeans.fwzx.service.ProjectService;
//import cn.geobeans.fwzx.service.UserService;
//import cn.geobeans.fwzx.util.StringUtil;
//
///**
// * @author liuxi E-mail:15895982509@163.com
// * @version 创建时间:2016-3-29下午8:08:59
// */
//@Service
//public class ProjectServiceImpl implements ProjectService {
//    private static Logger logger = Logger.getLogger(ProjectServiceImpl.class);
//
//    @javax.annotation.Resource(name = "jdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @javax.annotation.Resource
//    private UserService userService;
//
//    public int insert(final Project model) {
//        String sql = "INSERT INTO FWZX_PROJECT(ID,IP,NAME,PORT,STATE,DESCRIPTION,PROVIDER,REG_TIME,FILE_NAME,FILE_PATH,CHECK_STATE,TEST_URL) VALUES(?,?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
//        int result = -1;
//        try {
//            if (getProjectByName(model.getName()) == null) {
//                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                    public void setValues(PreparedStatement ps) throws SQLException {
//                        ps.setString(1, model.getId());
//                        ps.setString(2, model.getIp());
//                        ps.setString(3, model.getName());
//                        ps.setString(4, model.getPort());
//                        ps.setString(5, model.getState());
//                        ps.setString(6, model.getDescription());
//                        ps.setString(7, model.getProvider());
//                        ps.setString(8, model.getRegTime());
//                        ps.setString(9, model.getFileName());
//                        ps.setString(10, model.getFilePath());
//                        ps.setString(11, model.getCheckState());
//                        ps.setString(12, model.getTestUrl());
//
//                    }
//                });
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    public int delete(final String id) {
//        String sql = "DELETE FROM FWZX_PROJECT WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, id);
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    public Project get(String id) {
//        String sql = "SELECT ID,IP,NAME,PORT,STATE,DESCRIPTION,PROVIDER,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss'),FILE_NAME,FILE_PATH,CHECK_STATE,TEST_URL FROM FWZX_PROJECT WHERE ID=?";
//        String usageSql = "SELECT U.ID,U.IP,U.NAME,U.DESCRIPTION FROM FWZX_USAGE U RIGHT JOIN FWZX_USAGE_PROJECT U_P ON U.Id = U_P.USAGE_ID WHERE U_P.PROJECT_ID=?";
//        Project model = null;
//        List<Usage> usageList = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<Project>(Project.class));
//            usageList = jdbcTemplate.query(usageSql, new Object[]{id}, new BeanPropertyRowMapper<Usage>(Usage.class));
//            model.setUsageList(usageList);
//        } catch (Exception e) {
//            return null;
//        }
//        return model;
//    }
//
//    public List<Project> findList() {
//        String sql = "SELECT ID,IP,NAME,PORT,STATE,DESCRIPTION,PROVIDER,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss'),FILE_NAME,FILE_PATH,CHECK_STATE,TEST_URL FROM FWZX_PROJECT";
//        List<Project> list = null;
//        try {
//            list = (List<Project>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<Project>(Project.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//    public Project getProjectByName(String name) {
//        String sql = "SELECT ID,IP,NAME,PORT,STATE,DESCRIPTION,PROVIDER,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss'),FILE_NAME,FILE_PATH,CHECK_STATE,TEST_URL FROM FWZX_PROJECT WHERE NAME=?";
//        Project model = null;
//        try {
//            model = jdbcTemplate.queryForObject(sql, new Object[]{name}, new BeanPropertyRowMapper<Project>(Project.class));
//        } catch (Exception e) {
//            return null;
//        }
//        return model;
//    }
//
//    public int update(final Project model) {
//        String sql = "UPDATE FWZX_PROJECT SET IP=?,NAME=?,PORT=?,STATE=?,DESCRIPTION=?,PROVIDER=?,FILE_NAME=?,FILE_PATH=?,CHECK_STATE=?,TEST_URL=? WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, model.getIp());
//                    ps.setString(2, model.getName());
//                    ps.setString(3, model.getPort());
//                    ps.setString(4, model.getState());
//                    ps.setString(5, model.getDescription());
//                    ps.setString(6, model.getProvider());
//                    ps.setString(7, model.getFileName());
//                    ps.setString(8, model.getFilePath());
//                    ps.setString(9, model.getCheckState());
//                    ps.setString(10, model.getTestUrl());
//                    ps.setString(11, model.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//    public List<Map<String, Object>> getListByNameOrProvider(String name, String provider, String userId) {
//        String sql = "SELECT ID,IP,NAME,PORT,STATE,DESCRIPTION,PROVIDER,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss'),FILE_NAME,FILE_PATH,CHECK_STATE,TEST_URL FROM FWZX_PROJECT WHERE 1=1";
//        List<Map<String, Object>> list = null;
//        List<String> params = new ArrayList<String>();
//        boolean hasCheck = false;
//        try {
//            List<Resource> resourceList = userService.getResourceById(userId);
//            for (int i = 0; !StringUtil.isListEmpty(resourceList) && i < resourceList.size(); i++) {
//                if (resourceList.get(i).getName().equals("check")) {
//                    hasCheck = true;
//                }
//            }
//            if (!hasCheck) {
//                sql = sql + " AND CHECK_STATE=?";
//                params.add("审核通过");
//            }
//            if (!StringUtil.isNull(name)) {
//                sql = sql + " AND NAME=?";
//                params.add(name);
//            }
//            if (!StringUtil.isNull(provider)) {
//                sql = sql + " AND PROVIDER=?";
//                params.add(provider);
//            }
//            sql = sql + " ORDER BY REG_TIME DESC";
//            list = jdbcTemplate.queryForList(sql, params.toArray());
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//
//    public List<String> getAllProviders() {
//        String sql = "SELECT DISTINCT PROVIDER FROM FWZX_PROJECT";
//        List<String> list = null;
//        try {
//            list = jdbcTemplate.queryForList(sql, String.class);
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//    }
//
//
//    public int getProjectCountByMonth(String yearMonth) {
//        String sql = "SELECT COUNT(ID) FROM FWZX_PROJECT WHERE TO_CHAR(REG_TIME,'yyyy-mm')=?";
//        int successCount = 0;
//        try {
//            successCount = jdbcTemplate.queryForObject(sql, new Object[]{yearMonth}, Integer.class);
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return successCount;
//    }
//
//    public int getProviderCountByMonth(String yearMonth) {
//        String sql = "SELECT COUNT(DISTINCT PROVIDER) FROM FWZX_PROJECT WHERE TO_CHAR(REG_TIME,'yyyy-mm')=?";
//        int successCount = 0;
//        try {
//            successCount = jdbcTemplate.queryForObject(sql, new Object[]{yearMonth}, Integer.class);
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return successCount;
//    }
//
//    public float getPercentOfProjectByYear(String year, String provider) {
//        String getProjectsByYearSql = "SELECT COUNT(ID) FROM FWZX_PROJECT WHERE TO_CHAR(REG_TIME,'yyyy')=?";
//        String getProjectsByYearProviderSql = "SELECT COUNT(ID) FROM FWZX_PROJECT WHERE TO_CHAR(REG_TIME,'yyyy')=? AND PROVIDER=?";
//        int projects = 0;
//        int providerProject = 0;
//        try {
//            projects = jdbcTemplate.queryForObject(getProjectsByYearSql, new Object[]{year}, Integer.class);
//            providerProject = jdbcTemplate.queryForObject(getProjectsByYearProviderSql, new Object[]{year, provider}, Integer.class);
//            if (projects == 0 || projects < providerProject) {
//                return 0;
//            } else {
//                float result = (float) providerProject / projects;
//                return result * 100;
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return 0;
//    }
//
//
//    public List<String> getProvidersByYear(String year) {
//        String sql = "SELECT DISTINCT PROVIDER FROM FWZX_PROJECT WHERE TO_CHAR(REG_TIME,'yyyy')=?";
//        List<String> list = null;
//        try {
//            list = jdbcTemplate.queryForList(sql, new Object[]{year}, String.class);
//        } catch (Exception e) {
//            return null;
//        }
//        return list;
//
//    }
//
//
//    public float getPercentOfUsages(String projectId) {
//        String getCountUsagesSql = "SELECT COUNT(*) FROM FWZX_USAGE_PROJECT";
//        String getCountUsageProjectSql = "SELECT COUNT(PROJECT_ID) FROM FWZX_USAGE_PROJECT WHERE PROJECT_ID=?";
//        int usagesCount = 0;
//        int usagesProject = 0;
//        projectId = StringUtil.isNull(projectId) ? "" : projectId;
//        try {
//            usagesCount = jdbcTemplate.queryForObject(getCountUsagesSql, new Object[]{}, Integer.class);
//            usagesProject = jdbcTemplate.queryForObject(getCountUsageProjectSql, new Object[]{projectId}, Integer.class);
//            if (usagesCount == 0 || usagesCount < usagesProject) {
//                return 0;
//            } else {
//                float result = (float) usagesProject / usagesCount;
//                return result * 100;
//            }
//
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return 0;
//
//    }
//
//    public int updataProjectState(final Project model) {
//        String sql = "UPDATE FWZX_PROJECT SET STATE=? WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, model.getState());
//                    ps.setString(2, model.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//
//    }
//
//    /**
//     * 更新应用的审核状态
//     *
//     * @param model
//     */
//    @Override
//    public int updateProjectCheckState(final Project model) {
//        String sql = "UPDATE FWZX_PROJECT SET CHECK_STATE=? WHERE ID=?";
//        int result = -1;
//        try {
//            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//                public void setValues(PreparedStatement ps) throws SQLException {
//                    ps.setString(1, model.getCheckState());
//                    ps.setString(2, model.getId());
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return result;
//    }
//
//}
