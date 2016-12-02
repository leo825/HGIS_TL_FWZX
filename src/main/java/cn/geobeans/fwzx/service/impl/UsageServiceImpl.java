package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.UsageService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-1下午12:40:19
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class UsageServiceImpl implements UsageService {

    private static Logger logger = Logger.getLogger(UsageServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final UsageModel model) {
        String sql = "INSERT INTO FWZX_USAGE(ID,IP,NAME,DESCRIPTION) VALUES(?,?,?,?)";
        int result = -1;
        try {
            if (get(model.getId()) == null) {
                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, model.getId());
                        ps.setString(2, model.getIp());
                        ps.setString(3, model.getName());
                        ps.setString(4, model.getDescription());
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int delete(final String id, final String projectId) {
        String sql = "DELETE FROM FWZX_USAGE WHERE ID=?";
        String usageProjectSql = "DELETE FROM FWZX_USAGE_PROJECT WHERE USAGE_ID=? AND PROJECT_ID=?";
        int result = -1;
        try {
            if (StringUtil.isNull(projectId)) {
                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, id);
                    }
                });
            } else {
                result = jdbcTemplate.update(usageProjectSql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, id);
                        ps.setString(2, projectId);
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public UsageModel get(String id) {
        String sql = "SELECT ID,IP,NAME,DESCRIPTION FROM FWZX_USAGE WHERE ID=?";
        String projectSql = "SELECT P.ID,P.IP,P.NAME,P.PORT,P.STATE,P.DESCRIPTION,P.PROVIDER,TO_CHAR(P.REG_TIME,'yyyy-mm-dd hh24:mi:ss'),P.FILE_NAME, P.FILE_PATH,P.CHECK_STATE,P.TEST_URL FROM FWZX_PROJECT P RIGHT JOIN FWZX_USAGE_PROJECT U_P ON P.ID = U_P.PROJECT_ID WHERE U_P.USAGE_ID=?";
        UsageModel model = null;
        List<ProjectModel> projectList = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<UsageModel>(UsageModel.class));
            projectList = (List<ProjectModel>) jdbcTemplate.query(projectSql, new Object[]{id}, new BeanPropertyRowMapper<ProjectModel>(ProjectModel.class));
            model.setProjectList(projectList);
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    public List<UsageModel> findList() {
        String sql = "SELECT ID,IP,NAME,DESCRIPTION FROM FWZX_USAGE";
        String projectSql = "SELECT P.ID,P.IP,P.NAME,P.PORT,P.STATE,P.DESCRIPTION,P.PROVIDER,TO_CHAR(P.REG_TIME,'yyyy-mm-dd hh24:mi:ss'),P.FILE_NAME,P.FILE_PATH,P.CHECK_STATE,P.TEST_URL FROM FWZX_PROJECT P RIGHT JOIN FWZX_USAGE_PROJECT U_P ON P.ID = U_P.PROJECT_ID WHERE U_P.USAGE_ID=?";
        List<UsageModel> list = null;
        List<ProjectModel> projectList = null;
        try {
            list = (List<UsageModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<UsageModel>(UsageModel.class));
            for (int i = 0; !StringUtil.isListEmpty(list) && i < list.size(); i++) {
                projectList = (List<ProjectModel>) jdbcTemplate.query(projectSql, new Object[]{list.get(i).getId()}, new BeanPropertyRowMapper<ProjectModel>(ProjectModel.class));
                if (!StringUtil.isListEmpty(projectList)) {
                    list.get(i).setProjectList(projectList);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    public int update(final UsageModel model) {
        String sql = "UPDATE FWZX_USAGE SET IP=?,NAME=?,DESCRIPTION=? WHERE ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, model.getIp());
                    ps.setString(2, model.getName());
                    ps.setString(3, model.getDescription());
                    ps.setString(4, model.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public UsageModel getByIp(String ip) {
        String sql = "SELECT ID,IP,NAME,DESCRIPTION FROM FWZX_USAGE WHERE IP=?";
        UsageModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{ip}, new BeanPropertyRowMapper<UsageModel>(UsageModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    @Override
    public List<UsageModel> getListByParms(String ip, String name) {
        String sql = "SELECT ID,IP,NAME,DESCRIPTION FROM FWZX_USAGE WHERE 1=1";
        List<UsageModel> list = null;
        List<String> params = new ArrayList<String>();
        try {
            if (!StringUtil.isNull(ip)) {
                sql = sql + " AND IP=?";
                params.add(ip);
            }
            if (!StringUtil.isNull(name)) {
                sql = sql + " AND NAME LIKE '%"+name+"%'";
                params.add(name);
            }
            list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<UsageModel>(UsageModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }
}
