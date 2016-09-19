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
import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.RoleResourceService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-27上午8:53:13
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class RoleResourceServiceImpl implements RoleResourceService {

    private static Logger logger = Logger.getLogger(RoleResourceServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final RoleModel role, final ResourceModel resource) {

        String sql = "INSERT INTO FWZX_ROLE_RESOURCE(ROLE_ID,RESOURCE_ID) VALUES(?,?)";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, role.getId());
                    ps.setString(2, resource.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    @Override
    public int delete(final RoleModel role, final ResourceModel resource) {
        String sql = "DELETE FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=? AND RESOURCE_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedstatement) throws SQLException {
                    preparedstatement.setString(1, role.getId());
                    preparedstatement.setString(2, resource.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int update(final RoleModel oldRole, final ResourceModel oldResource, final RoleModel newRole, final ResourceModel newResouce) {
        String sql = "UPDATE FWZX_USAGE_PROJECT SET USAGE_ID=?,PROJECT_ID=? WHERE USAGE_ID=? AND PROJECT_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, newRole.getId());
                    ps.setString(2, newResouce.getId());
                    ps.setString(3, oldRole.getId());
                    ps.setString(4, oldResource.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public List<ResourceModel> getResourceListByRoleId(String roleId) {
        String sql = "SELECT RES.ID,RES.PARENT_ID,RES.NAME,RES.NICKNAME,RES.DESCRIPTION FROM (SELECT RESOURCE_ID FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=?) R_RES INNER JOIN FWZX_RESOURCE RES ON R_RES.RESOURCE_ID=RES.ID";
        List<ResourceModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new Object[]{roleId}, new BeanPropertyRowMapper<ResourceModel>(ResourceModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public List<RoleModel> getRoleListByResourceId(String resourceId) {
        String sql = "SELECT R.ID,R.NAME,R.DESCRIPTION FROM (SELECT ROLE_ID FROM FWZX_ROLE_RESOURCE WHERE RESOURCE_ID=?) R_RES INNER JOIN FWZX_ROLE R ON R_RES.ROLE_ID=R.ID";
        List<RoleModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new Object[]{resourceId}, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public boolean isRoleResourceExist(RoleModel role, ResourceModel resource) {
        String sql = "SELECT ROLE_ID,RESOURCE_ID FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=? AND RESOURCE_ID=?";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{role.getId(), resource.getId()});
            if (!StringUtil.isListEmpty(list)) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    @Override
    public int deleteAllRoleResourceByRoleId(final String roleId) {
        String sql = "DELETE FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedstatement) throws SQLException {
                    preparedstatement.setString(1, roleId);
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }
}
