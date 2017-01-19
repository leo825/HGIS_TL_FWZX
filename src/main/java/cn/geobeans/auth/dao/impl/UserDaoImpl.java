package cn.geobeans.auth.dao.impl;

import cn.geobeans.auth.dao.UserDao;
import cn.geobeans.auth.model.Role;
import cn.geobeans.auth.model.User;
import cn.geobeans.auth.model.UserRole;
import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.common.model.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"unchecked", "JpaQlInspection"})
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {


    @Override
    public List<Role> listUserRoles(String userId) {
        String hql = "select ur.role from UserRole ur where ur.user.id=?";
        return this.getSession().createQuery(hql).setParameter(0, userId).list();
    }

    @Override
    public List<String> listUserRoleIds(String userId) {
        String hql = "select ur.role.id from UserRole ur where ur.user.id=?";
        return this.getSession().createQuery(hql).setParameter(0, userId).list();
    }


    @Override
    public List<String> listUserGroupIds(String userId) {
        String hql = "select ug.group.id from UserGroup ug where ug.user.id=?";
        return this.getSession().createQuery(hql).setParameter(0, userId).list();
    }

    @Override
    public UserRole loadUserRole(String userId, String roleId) {
        String hql = "select ur from UserRole ur left join fetch ur.user u left join fetch ur.role r where u.id=? and r.id=?";
        return (UserRole) this.getSession().createQuery(hql).setParameter(0, userId).setParameter(1, roleId).uniqueResult();
    }


    @Override
    public User loadByUsername(String username) {
        String hql = "from User where username=?";
        return (User) this.queryObject(hql, username);
    }

    @Override
    public List<User> listRoleUsers(String roleId) {
        String hql = "select ur.user from UserRole ur where ur.role.id=?";
        return this.list(hql, roleId);
    }


    @Override
    public List<User> listGroupUsers(String gid) {
        String hql = "select ug.user from UserGroup ug where ug.group.id=?";
        return this.list(hql, gid);
    }

    @Override
    public void addUserRole(User user, Role role) {
        UserRole ur = this.loadUserRole(user.getId(), role.getId());
        if (ur != null) return;
        ur = new UserRole();
        ur.setRole(role);
        ur.setUser(user);
        this.getSession().save(ur);
    }


    @Override
    public void deleteUserRoles(String uid) {
        String hql = "delete UserRole ur where ur.user.id=?";
        this.updateByHql(hql, uid);
    }

    @Override
    public void deleteUserGroups(String gid) {
        String hql = "delete UserGroup ug where ug.user.id=?";
        this.updateByHql(hql, gid);
    }

    @Override
    public Pager<User> findUser() {
        return this.find("from User");
    }

    @Override
    public void deleteUserRole(String uid, String rid) {
        String hql = "delete UserRole ur where ur.user.id=? and ur.role.id=?";
        this.updateByHql(hql, new Object[]{uid, rid});
    }

    @Override
    public void deleteUserGroup(String uid, String gid) {
        String hql = "delete UserGroup ug where ug.user.id=? and ug.group.id=?";
        this.updateByHql(hql, new Object[]{uid, gid});
    }
}
