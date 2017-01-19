package cn.geobeans.auth.dao.impl;


import cn.geobeans.auth.dao.RoleDao;
import cn.geobeans.auth.model.Role;
import cn.geobeans.common.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	@Override
	public List<Role> listRole() {
		return this.list("from Role");
	}

	@Override
	public void deleteRoleUsers(String rid) {
		this.updateByHql("delete UserRole ur where ur.role.id=?",rid);
	}


}
