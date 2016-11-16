package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.geobeans.fwzx.model.ProjectModel;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.service.RouteService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-28下午2:41:29
 */
@Service
public class RouteServiceImpl implements RouteService {
    private static Logger logger = Logger.getLogger(RouteServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public int insert(final RouteModel model) {
        String sql = "INSERT INTO FWZX_ROUTE(ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,REG_TIME) VALUES(?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'))";
        int result = -1;
        try {
            if (getListByNameOrProjectName(model.getServerName(),model.getProjectId()).size() == 0) {
                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, model.getId());
                        ps.setString(2, model.getProjectId());
                        ps.setString(3, model.getServerName());
                        ps.setString(4, model.getServerAddr());
                        ps.setString(5, model.getDataReturnType());
                        ps.setString(6, model.getDescription());
                        ps.setString(7, model.getRegTime());
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    public int delete(final String id) {
        String sql = "DELETE FROM FWZX_ROUTE WHERE ID=?";
        int result = -1;
        try {

            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, id);
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    public RouteModel get(String id) {
        String sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE WHERE ID=?";
        RouteModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<RouteModel>(RouteModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    public List<RouteModel> findList() {
        String sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE";
        List<RouteModel> list = null;
        try {
            list = (List<RouteModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<RouteModel>(RouteModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public RouteModel getByServerName(String serverName) {
        String sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE WHERE SERVER_NAME=?";
        RouteModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{serverName}, new BeanPropertyRowMapper<RouteModel>(RouteModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    public int update(final RouteModel model) {
        String sql = "UPDATE FWZX_ROUTE SET PROJECT_ID=?,SERVER_NAME=?,SERVER_ADDR=?,DATA_RETURN_TYPE=?,DESCRIPTION=? WHERE ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, model.getProjectId());
                    ps.setString(2, model.getServerName());
                    ps.setString(3, model.getServerAddr());
                    ps.setString(4, model.getDataReturnType());
                    ps.setString(5, model.getDescription());
                    ps.setString(6, model.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getListByNameOrProjectName(String name, String projectId) {
        String sql = "";
        List<Map<String, Object>> list = null;
        try {
            if (!StringUtil.isNull(name) && !StringUtil.isNull(projectId)) {
                sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE WHERE SERVER_NAME=? AND PROJECT_ID=?";
                list = jdbcTemplate.queryForList(sql, new Object[]{name, projectId});
            } else if (!StringUtil.isNull(name)) {
                sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE WHERE SERVER_NAME=?";
                list = jdbcTemplate.queryForList(sql, new Object[]{name});
            } else if (!StringUtil.isNull(projectId)) {
                sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE WHERE PROJECT_ID=?";
                list = jdbcTemplate.queryForList(sql, new Object[]{projectId});
            } else {
                sql = "SELECT ID,PROJECT_ID,SERVER_NAME,SERVER_ADDR,DATA_RETURN_TYPE,DESCRIPTION,TO_CHAR(REG_TIME,'yyyy-mm-dd hh24:mi:ss') FROM FWZX_ROUTE";
                list = jdbcTemplate.queryForList(sql, new Object[]{});
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

}
