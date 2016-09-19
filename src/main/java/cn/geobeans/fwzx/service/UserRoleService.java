package cn.geobeans.fwzx.service;

import java.util.List;

import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UserModel;

/**
 * @author liuxi
 * @version 创建时间:2016-6-2下午6:41:56
 * @parameter E-mail:15895982509@163.com
 */
public interface UserRoleService {

    /**
     * 插入一个用户使用的角色
     *
     * @param user
     * @param role
     * @return int
     */
    public int insert(UserModel user, RoleModel role);

    /**
     * 删除用户角色
     *
     * @param user
     * @param role
     * @return int
     */
    public int delete(UserModel user, RoleModel role);

    /**
     * 更新用户角色
     *
     * @param user
     * @param role
     * @return int
     */
    public int update(UserModel oldUser, RoleModel oldRole, UserModel newUser, RoleModel newRole);

    /**
     * 通过userId查找所有角色
     *
     * @param userId
     * @return List< RoleModel >
     */
    public List<RoleModel> getRoleListByUserId(String userId);

    /**
     * 通过roleId查找所有用户
     *
     * @param roleId
     * @return List< UserModel >
     */
    public List<UserModel> getUserListByRoleId(String roleId);


    /**
     * 查看数据库中是否存在此记录
     *
     * @param user
     * @param role
     * @return boolean
     */
    public boolean isUserRoleExist(UserModel user, RoleModel role);

    /**
     * 通过userId删除所有userId对应的Role
     *
     * @param userId 用户id
     * @return int
     */
    public int deleteAllUserRoleByUserId(String userId);

}
