package cn.geobeans.auth.service.impl;

import cn.geobeans.auth.dao.AclDao;
import cn.geobeans.auth.model.Acl;
import cn.geobeans.auth.model.ControllerResources;
import cn.geobeans.auth.model.Role;
import cn.geobeans.auth.model.User;
import cn.geobeans.auth.service.AclService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service("aclService")
public class AclServiceImpl implements AclService {
    @Inject
    private AclDao aclDao;

    @Override
    public Acl loadAcl(String pid, String ptype, String rid, String rtype) {
        return aclDao.loadAcl(pid, ptype, rid, rtype);
    }

    @Override
    public Acl loadAclByRole(String roleId, String rid, String rtype) {
        return aclDao.loadAcl(roleId, Role.PRINCIPAL_TYPE, rid, rtype);
    }

    @Override
    public Acl loadAclByUser(String userId, String rid, String rtype) {
        return aclDao.loadAcl(userId, User.PRINCIPAL_TYPE, rid, rtype);
    }

    @Override
    public List<String> listRoleOperIdsByRes(String rid, String rtype,
                                             String roleId) {
        return aclDao.listRoleOperIdsByRes(rid, rtype, roleId);
    }

    @Override
    public List<String> listUserOperIdsByRes(String rid, String rtype,
                                             String userId) {
        return aclDao.listUserOperIdsByRes(rid, rtype, userId);
    }

    @Override
    public List<String> listUserSelfOperIdsByRes(String rid, String rtype,
                                                 String userId) {
        return aclDao.listUserSelfOperIdsByRes(rid, rtype, userId);
    }

    @Override
    public Map<String, List<String>> listAllControllerResAndOperByRole(String roleId) {
        return aclDao.listAllResAndOperByRole(roleId, ControllerResources.RES_TYPE);
    }

    @Override
    public Map<String, List<String>> listAllControllerResAndOperByUser(String userId) {
        return aclDao.listAllResAndOperByUser(userId, ControllerResources.RES_TYPE);
    }

    @Override
    public List<String> listMenuSnByRole(String roleId) {
        return aclDao.listMenuSnByRole(roleId);
    }

    @Override
    public List<String> listMenuSnByUser(String userId) {
        return aclDao.listMenuSnByUser(userId);
    }

    @Override
    public List<String> listMenuIdByRole(String roleId) {
        return aclDao.listMenuIdByRole(roleId);
    }

    @Override
    public List<String> listMenuIdByUser(String userId) {
        return aclDao.listMenuIdByUser(userId);
    }

    @Override
    public List<String> listMenuIdByUserSelf(String userId) {
        return aclDao.listMenuIdByUserSelf(userId);
    }

    @Override
    public void add(Acl acl) {
        aclDao.add(acl);
    }
}
