package cn.geobeans.fwzx.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.service.OperationService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-4-25下午11:43:26
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class OperationServiceImpl implements OperationService {

    private static Logger logger = Logger.getLogger(OperationServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final OperationModel model) {
        String sql = "INSERT INTO FWZX_OPERATION(ID,IP,USER_NAME,SERVER_NAME,PROJECT_NAME,RESULT,OPERATE_TIME,DESCRIPTION) VALUES(?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        int result = -1;
        try {

            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, model.getId());
                    ps.setString(2, model.getIp());
                    ps.setString(3, model.getUserName());
                    ps.setString(4, model.getServerName());
                    ps.setString(5, model.getProjectName());
                    ps.setString(6, model.getResult());
                    ps.setString(7, model.getOperateTime());
                    ps.setString(8, model.getDescription());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int delete(final String id) {
        String sql = "DELETE FROM FWZX_OPERATION WHERE ID=?";
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
    public OperationModel get(String id) {
        String sql = "SELECT ID,IP,USER_NAME,SERVER_NAME,PROJECT_NAME,RESULT,TO_CHAR(OPERATE_TIME,'yyyy-mm-dd hh24:mi:ss'),DESCRIPTION FROM FWZX_OPERATION WHERE ID=?";
        OperationModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<OperationModel>(OperationModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    @Override
    public List<OperationModel> findList() {
        String sql = "SELECT ID,IP,USER_NAME,SERVER_NAME,PROJECT_NAME,RESULT,TO_CHAR(OPERATE_TIME,'yyyy-mm-dd hh24:mi:ss'),DESCRIPTION FROM FWZX_OPERATION";
        List<OperationModel> list = null;
        try {
            list = (List<OperationModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<OperationModel>(OperationModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public int getAllCount(String projectId) {
        String sql = "SELECT COUNT(PROJECT_ID) FROM FWZX_OPERATION WHERE PROJECT_ID=?";
        int allCount = 0;
        try {
            allCount = jdbcTemplate.queryForObject(sql, new Object[]{projectId}, Integer.class);
        } catch (Exception e) {
            logger.error(e);
        }
        return allCount;
    }

    @Override
    public int getSuccessCount(String projectId, String result) {
        String sql = "SELECT COUNT(PROJECT_ID) FROM FWZX_OPERATION WHERE PROJECT_ID=? AND RESULT=?";
        int successCount = 0;
        try {
            successCount = jdbcTemplate.queryForObject(sql, new Object[]{projectId, result}, Integer.class);
        } catch (Exception e) {
            logger.error(e);
        }
        return successCount;
    }

    @Override
    public int getCountBySecondOfProject(String projectId, String operationTime) {
        String sql = "SELECT COUNT(PROJECT_ID) FROM FWZX_OPERATION WHERE PROJECT_ID=? AND OPERATE_TIME=TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, new Object[]{projectId, operationTime}, Integer.class);
        } catch (Exception e) {
            logger.error(e);
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> getListByParms(String result, String projectName, String startTime, String endTime) {
        String sql = "SELECT ID,IP,USER_NAME,SERVER_NAME,PROJECT_NAME,RESULT,TO_CHAR(OPERATE_TIME,'yyyy-mm-dd hh24:mi:ss'),DESCRIPTION FROM FWZX_OPERATION WHERE 1=1";
        List<Map<String, Object>> list = null;
        List<String> params = new ArrayList<String>();
        try {
            if (!StringUtil.isNull(result)) {
                sql = sql + " AND RESULT=?";
                params.add(result);
            }
            if (!StringUtil.isNull(projectName)) {
                sql = sql + " AND PROJECT_NAME=?";
                params.add(projectName);

            }
            if (!StringUtil.isNull(startTime) && StringUtil.isNull(endTime)) {
                endTime = CalendarUtil.getDataTimeStr();
                sql = sql + " AND OPERATE_TIME BETWEEN TO_DATE(?,'yyyy-mm-dd hh24:mi:ss') AND TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
                params.add(startTime);
                params.add(endTime);
            }
            if (!StringUtil.isNull(startTime) && !StringUtil.isNull(endTime)) {
                sql = sql + " AND OPERATE_TIME BETWEEN TO_DATE(?,'yyyy-mm-dd hh24:mi:ss') AND TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
                params.add(startTime);
                params.add(endTime);
            }

            list = jdbcTemplate.queryForList(sql, params.toArray());
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public int getCountOfProjectByTime(String projectId, String operateTime) {
        String sql = "";
        int count = 0;
        try {
            if (!StringUtil.isNull(projectId)) {
                sql = "SELECT COUNT(ID) FROM FWZX_OPERATION WHERE ID=? AND TO_CHAR(OPERATE_TIME,'yyyy-mm-dd hh24:mi:ss')=?";
                count = jdbcTemplate.queryForObject(sql, new Object[]{projectId, operateTime}, Integer.class);
            } else {
                sql = "SELECT COUNT(ID) FROM FWZX_OPERATION WHERE TO_CHAR(OPERATE_TIME,'yyyy-mm-dd hh24:mi:ss')=?";
                count = jdbcTemplate.queryForObject(sql, new Object[]{operateTime}, Integer.class);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return count;
    }

}
