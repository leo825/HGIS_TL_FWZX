package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.UsageProjectService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-26上午10:40:54
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class UsageProjectServiceImpl implements UsageProjectService {

    private static Logger logger = Logger.getLogger(UsageProjectServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final UsageModel usage, final ProjectModel project) {

        String sql = "INSERT INTO FWZX_USAGE_PROJECT(USAGE_ID,PROJECT_ID) VALUES(?,?)";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, usage.getId());
                    ps.setString(2, project.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    @Override
    public int delete(final UsageModel usage, final ProjectModel project) {
        String sql = "DELETE FROM FWZX_USAGE_PROJECT WHERE USAGE_ID=? AND PROJECT_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedstatement) throws SQLException {
                    preparedstatement.setString(1, usage.getId());
                    preparedstatement.setString(2, project.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int update(final UsageModel oldUsage, final ProjectModel oldProject, final UsageModel newUsage, final ProjectModel newProject) {
        String sql = "UPDATE FWZX_USAGE_PROJECT SET USAGE_ID=?,PROJECT_ID=? WHERE USAGE_ID=? AND PROJECT_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, newUsage.getId());
                    ps.setString(2, newProject.getId());
                    ps.setString(3, oldUsage.getId());
                    ps.setString(4, oldProject.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public List<ProjectModel> getProjectListByUsageId(String usageId) {
        String sql = "SELECT P.ID,P.IP,P.NAME,P.PORT,P.STATE,P.DESCRIPTION,P.PROVIDER,TO_CHAR(P.REG_TIME,'yyyy-mm-dd hh24:mi:ss'),TO_CHAR(P.Reg_Time,'yyyy-mm-dd hh24:mi:ss'),P.FILE_NAME,P.FILE_PATH,P.CHECK_STATE,P.TEST_URL " + "FROM (SELECT PROJECT_ID FROM FWZX_USAGE_PROJECT WHERE USAGE_ID='" + usageId + "') U_P INNER JOIN FWZX_PROJECT P ON U_P.PROJECT_ID=P.ID";
        List<ProjectModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ProjectModel>(ProjectModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public List<UsageModel> getUsageListByProjectId(String projectId) {
        String sql = "SELECT U.ID,U.IP,U.NAME,U.DESCRIPTION " + "FROM (SELECT USAGE_ID FROM FWZX_USAGE_PROJECT WHERE PROJECT_ID='" + projectId + "') U_P INNER JOIN FWZX_USAGE U ON U_P.USAGE_ID=U.ID";
        List<UsageModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UsageModel>(UsageModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public boolean isUsageProjectExist(String usageId, String projectId) {
        String sql = "SELECT USAGE_ID,PROJECT_ID FROM FWZX_USAGE_PROJECT WHERE USAGE_ID='" + usageId + "'" + " AND PROJECT_ID='" + projectId + "'";
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
    public List<UsageModel> getUsageListByParams(String ip, String name, String projectId) {

        String sql = "SELECT ID,IP,NAME,DESCRIPTION FROM FWZX_USAGE WHERE 1=1";
        String projectSql = "SELECT P.ID,P.IP,P.NAME,P.PORT,P.STATE,P.DESCRIPTION,P.PROVIDER,TO_CHAR(P.REG_TIME,'yyyy-mm-dd hh24:mi:ss'),P.FILE_NAME,P.FILE_PATH,P.CHECK_STATE,P.TEST_URL FROM FWZX_PROJECT P RIGHT JOIN FWZX_USAGE_PROJECT U_P ON P.ID = U_P.PROJECT_ID WHERE 1=1";
        List<UsageModel> list = null;
        List<ProjectModel> projectList = null;
        List<String> paramsSql = new ArrayList<String>();
        try {
            if (!StringUtil.isNull(ip)) {
                sql = sql + " AND IP='" + ip + "'";
            }
            if (!StringUtil.isNull(name)) {
                sql = sql + " AND NAME LIKE '%" + name + "%'";
            }

            if (!StringUtil.isNull(projectId)) {
                projectSql = projectSql + " AND U_P.PROJECT_ID='" + projectId + "'";
            }
            projectSql = projectSql + " AND U_P.USAGE_ID=";
            list = (List<UsageModel>) jdbcTemplate.query(sql, paramsSql.toArray(), new BeanPropertyRowMapper<UsageModel>(UsageModel.class));

            for (int i = 0; !StringUtil.isListEmpty(list) && i < list.size(); i++) {
                String temp_sql = projectSql + "?";
                projectList = jdbcTemplate.query(temp_sql, new Object[]{list.get(i).getId()}, new BeanPropertyRowMapper<ProjectModel>(ProjectModel.class));
                if (!StringUtil.isListEmpty(projectList)) {
                    list.get(i).setProjectList(projectList);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }



}
