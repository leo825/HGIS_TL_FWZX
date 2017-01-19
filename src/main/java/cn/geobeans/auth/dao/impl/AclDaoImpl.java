package cn.geobeans.auth.dao.impl;

import cn.geobeans.auth.dao.AclDao;
import cn.geobeans.auth.model.Acl;
import cn.geobeans.auth.model.MenuResources;
import cn.geobeans.auth.model.Role;
import cn.geobeans.auth.model.User;
import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.kit.BasicSysKit;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("aclDao")
public class AclDaoImpl extends BaseDaoImpl<Acl> implements AclDao {

    @Override
    public Acl loadAcl(String pid, String ptype, String rid, String rtype) {
        String hql = "select a from Acl a where a.pid=? and a.ptype=? and a.rid=? and a.rtype=?";
        return (Acl) super.queryObject(hql, pid, ptype, rid, rtype);
    }

    private List<String> listOperIdsByResPrin(String rid, String rtype, String pid, String ptype) {
        String hql = "select a.aclState,co.id,co.indexPos from Acl a,ControllerOper co where " +
                "a.rid=co.rid and a.rid=? and a.rtype=? and a.pid=? and a.ptype=?";
        List<Object[]> objs = super.listObj(hql, rid, rtype, pid, ptype);
        return getOperIds(objs);

    }

    private List<String> getOperIds(List<Object[]> objs) {
        List<String> ids = new ArrayList<String>();
        int aclState = 0;
        String coid = "";
        int indexPos = 0;
        for (Object[] aobj : objs) {
            aclState = (Integer) aobj[0];
            coid = (String) aobj[1];
            indexPos = (Integer) aobj[2];
            if (Acl.checkPermission(indexPos, aclState)) {
                ids.add(coid);
            }
        }
        return ids;
    }

    @Override
    public List<String> listRoleOperIdsByRes(String rid, String rtype, String roleId) {
        return listOperIdsByResPrin(rid, rtype, roleId, Role.PRINCIPAL_TYPE);
    }

    @Override
    public List<String> listUserOperIdsByRes(String rid, String rtype, String userId) {
        List<String> ids = new ArrayList<String>();
        List<String> uids = listUserSelfOperIdsByRes(rid, rtype, userId);
        ids.addAll(uids);
        String hql = "select a.aclState,co.id,co.indexPos from Acl a,ControllerOper co,UserRole ur where " +
                "a.rid=co.rid and ur.role.id=a.pid and ur.user.id=? and a.rid=? and a.rtype=? and a.ptype=?";
        List<Object[]> objs = super.listObj(hql, userId, rid, rtype, Role.PRINCIPAL_TYPE);
        List<String> rids = getOperIds(objs);
        BasicSysKit.mergeList(ids, rids);
        return ids;
    }

    @Override
    public List<String> listUserSelfOperIdsByRes(String rid, String rtype, String userId) {
        return listOperIdsByResPrin(rid, rtype, userId, User.PRINCIPAL_TYPE);
    }

    private Map<String, List<String>> listAllResAndOperByPrin(String pid, String ptype, String rtype) {
        String hql = "select a.aclState,co.indexPos,cr.className,co.methodName from Acl a,ControllerResources cr,ControllerOper co where" +
                "a.rid=cr.id and cr.id=co.rid and a.pid=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, pid, ptype, rtype);
        return getResAndOper(objs);
    }

    private Map<String, List<String>> getResAndOper(List<Object[]> objs) {
        int aclState = 0;
        int indexPos = 0;
        String className = null;
        String methodName = null;
        Map<String, List<String>> maps = new HashMap<String, List<String>>();
        for (Object[] aobj : objs) {
            aclState = (Integer) aobj[0];
            indexPos = (Integer) aobj[1];
            className = (String) aobj[2];
            methodName = (String) aobj[3];
            if (Acl.checkPermission(indexPos, aclState)) {
                if (!maps.containsKey(className)) {
                    maps.put(className, new ArrayList<String>());
                    addMethodToList(maps, className, methodName);
                } else {
                    addMethodToList(maps, className, methodName);
                }
            }
        }
        return maps;
    }

    private void addMethodToList(Map<String, List<String>> maps, String className, String methodName) {
        List<String> ms = maps.get(className);
        String[] mns = methodName.split("\\|");
        for (String m : mns) {
            if (!ms.contains(m)) ms.add(m);
        }
    }

    @Override
    public Map<String, List<String>> listAllResAndOperByRole(String roleId, String rtype) {//通过角色id和资源类型获取所有的ControllerRes和ControllerOper
        return listAllResAndOperByPrin(roleId, Role.PRINCIPAL_TYPE, rtype);
    }

    @Override
    public Map<String, List<String>> listAllResAndOperByUser(String userId, String rtype) {
        Map<String, List<String>> umaps = listAllResAndOperByPrin(userId, User.PRINCIPAL_TYPE, rtype);//获取ControllerRes和ControllerOper
        String hql = "select a.aclState,co.indexPos,cr.className,co.methodName from Acl a,ControllerResources cr,ControllerOper co,UserRole ur where" +
                "a.rid=cr.id and cr.id=co.rid and ur.role.id=a.pid and ur.user.id=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, userId, Role.PRINCIPAL_TYPE, rtype);
        Map<String, List<String>> rmaps = getResAndOper(objs);
        Set<String> rkeys = rmaps.keySet();
        for (String rk : rkeys) {
            if (umaps.containsKey(rk)) {
                BasicSysKit.mergeList(umaps.get(rk), rmaps.get(rk));
            } else {
                umaps.put(rk, rmaps.get(rkeys));
            }
        }
        return umaps;
    }

    /**
     * 通过主体id获取MenuRes的列表
     *
     * @param pid   主体id
     * @param ptype 主题类型
     * @return 返回MenuRes的sn的list
     */
    private List<String> listMenuSnByPrin(String pid, String ptype) {
        String hql = "select a.aclState,mr.sn from Acl a,MenuResources mr where a.rid=mr.id and a.pid=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, pid, ptype, MenuResources.RES_TYPE);
        return getMenuSn(objs);
    }

    private List<String> getMenuSn(List<Object[]> objs) {
        int aclState = 0;
        String sn = null;
        List<String> sns = new ArrayList<String>();
        for (Object[] aobj : objs) {
            aclState = (Integer) aobj[0];
            sn = (String) aobj[1];
            if (Acl.checkPermission(0, aclState)) {
                sns.add(sn);
            }
        }
        return sns;
    }

    @Override
    public List<String> listMenuSnByRole(String roleId) {
        return listMenuSnByPrin(roleId, Role.PRINCIPAL_TYPE);
    }

    @Override
    public List<String> listMenuSnByUser(String userId) {
        List<String> usns = listMenuSnByPrin(userId, User.PRINCIPAL_TYPE);
        String hql = "select a.aclState,mr.sn from " +
                "Acl a,MenuResources mr,UserRole ur where a.rid=mr.id and ur.role.id=a.pid and ur.user.id=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, userId, Role.PRINCIPAL_TYPE, MenuResources.RES_TYPE);
        List<String> rsns = getMenuSn(objs);
        BasicSysKit.mergeList(usns, rsns);
        return usns;
    }

    private List<String> listMenuIdByPrin(String pid, String ptype) {
        String hql = "select a.aclState,a.pid from Acl a where a.pid=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, pid, ptype, MenuResources.RES_TYPE);
        return getMenuIds(objs);
    }

    private List<String> getMenuIds(List<Object[]> objs) {
        int aclState = 0;
        String id = "";
        List<String> ids = new ArrayList<String>();
        for (Object[] aobj : objs) {
            aclState = (Integer) aobj[0];
            id = (String) aobj[1];
            if (Acl.checkPermission(0, aclState)) {
                ids.add(id);
            }
        }
        return ids;
    }

    @Override
    public List<String> listMenuIdByRole(String roleId) {
        return listMenuIdByPrin(roleId, Role.PRINCIPAL_TYPE);
    }

    @Override
    public List<String> listMenuIdByUser(String userId) {
        List<String> uids = listMenuIdByPrin(userId, User.PRINCIPAL_TYPE);
        String hql = "select a.aclState,mr.sn from " +
                "Acl a,UserRole ur where ur.role.id=a.pid and ur.user.id=? and a.ptype=? and a.rtype=?";
        List<Object[]> objs = super.listObj(hql, userId, Role.PRINCIPAL_TYPE, MenuResources.RES_TYPE);
        List<String> rids = getMenuIds(objs);
        BasicSysKit.mergeList(uids, rids);
        return uids;
    }

    @Override
    public List<String> listMenuIdByUserSelf(String userId) {
        return listMenuIdByPrin(userId, User.PRINCIPAL_TYPE);
    }


}
