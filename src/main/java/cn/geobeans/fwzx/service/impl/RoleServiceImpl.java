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

import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.service.RoleService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-22下午4:35:33
 * @parameter E-mail:15895982509@163.com
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final RoleModel model) {
        String sql = "INSERT INTO FWZX_ROLE(ID,NAME,DESCRIPTION) VALUES(?,?,?)";
        int result = -1;
        try {
            if (getRoleByName(model.getName()) == null) {
                result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, model.getId());
                        ps.setString(2, model.getName());
                        ps.setString(3, model.getDescription());
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public int delete(final String id) {
        String sql = "DELETE FROM FWZX_ROLE WHERE ID=?";
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
    public RoleModel get(String id) {
        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE ID=?";
        RoleModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    @Override
    public int update(final RoleModel model) {
        String sql = "UPDATE FWZX_ROLE SET NAME=?,DESCRIPTION=? WHERE ID=?";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, model.getName());
                    ps.setString(2, model.getDescription());
                    ps.setString(3, model.getId());
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public List<RoleModel> findList() {
        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE";
        List<RoleModel> list = null;
        try {
            list = (List<RoleModel>) jdbcTemplate.query(sql, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public RoleModel getRoleByName(String roleName) {
        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE NAME=?";
        RoleModel model = null;
        try {
            model = jdbcTemplate.queryForObject(sql, new Object[]{roleName}, new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return model;
    }

    @Override
    public List<ResourceModel> getResourceByRoleId(String roleId) {
        String sql = "SELECT RES.ID,RES.PARENT_ID,RES.NAME,RES.NICKNAME,RES.DESCRIPTION FROM (SELECT RESOURCE_ID FROM FWZX_ROLE_RESOURCE WHERE ROLE_ID=?) R_R INNER JOIN FWZX_RESOURCE RES ON R_R.RESOURCE_ID=RES.ID";
        List<ResourceModel> list = null;
        try {
            list = jdbcTemplate.query(sql, new Object[]{roleId}, new BeanPropertyRowMapper<ResourceModel>(ResourceModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    @Override
    public List<RoleModel> getListByNameAndDescription(String name, String description) {
        String sql = "SELECT ID,NAME,DESCRIPTION FROM FWZX_ROLE WHERE 1=1";
        List<RoleModel> list = null;
        List<String> params = new ArrayList<String>();
        try {
            if (!StringUtil.isNull(name)) {
                sql = sql + " AND NAME=?";
                params.add(name);
            }
            if (!StringUtil.isNull(description)) {
                sql = sql + " AND DESCRIPTION LIKE '%?%'";
                params.add(description);
            }
            list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<RoleModel>(RoleModel.class));
        } catch (Exception e) {
            return null;
        }
        return list;
    }

}
