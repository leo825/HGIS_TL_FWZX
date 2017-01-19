package cn.geobeans.auth.dao;


import cn.geobeans.auth.model.Role;
import cn.geobeans.auth.model.User;
import cn.geobeans.auth.model.UserRole;
import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.common.model.Pager;

import java.util.List;

public interface UserDao extends BaseDao<User> {

    /**
     * 获取用户的所有角色信息
     *
     * @param userId
     * @return
     */
    public List<Role> listUserRoles(String userId);

    /**
     * 获取用户的所有角色的id
     *
     * @param userId
     * @return
     */
    public List<String> listUserRoleIds(String userId);

    /**
     * 获取用户的所有组的id
     *
     * @param userId
     * @return
     */
    public List<String> listUserGroupIds(String userId);

    /**
     * 根据用户和角色获取用户角色的关联对象
     *
     * @param userId
     * @param roleId
     * @return
     */
    public UserRole loadUserRole(String userId, String roleId);

    /**
     * 根据用户名获取用户对象
     *
     * @param username
     * @return
     */
    public User loadByUsername(String username);

    /**
     * 根据角色id获取用户列表
     *
     * @param roleId
     * @return
     */
    public List<User> listRoleUsers(String roleId);

    /**
     * 获取某个组中的用户对象
     *
     * @param gid
     * @return
     */
    public List<User> listGroupUsers(String gid);

    /**
     * 添加用户角色对象
     *
     * @param user
     * @param role
     */
    public void addUserRole(User user, Role role);

    /**
     * 删除用户的角色信息
     *
     * @param uid
     */
    public void deleteUserRoles(String uid);

    /**
     * 删除用户的组信息
     *
     * @param gid
     */
    public void deleteUserGroups(String gid);

    public Pager<User> findUser();

    /**
     * 删除用户角色对象
     *  @param uid
     * @param rid
     */
    public void deleteUserRole(String uid, String rid);

    /**
     * 删除用户组对象
     *  @param uid
     * @param gid
     */
    public void deleteUserGroup(String uid, String gid);

}
