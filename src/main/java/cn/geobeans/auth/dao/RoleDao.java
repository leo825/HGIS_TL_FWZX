package cn.geobeans.auth.dao;


import cn.geobeans.auth.model.Role;
import cn.geobeans.common.dao.BaseDao;

import java.util.List;


public interface RoleDao extends BaseDao<Role> {
    public List<Role> listRole();

    public void deleteRoleUsers(String rid);
}
