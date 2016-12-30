package cn.geobeans.fwzx.service;

import cn.geobeans.fwzx.model.Resource;
import cn.geobeans.fwzx.model.Role;

import java.util.List;

/**
 * @author liuxi
 * @version 创建时间:2016-5-27上午1:53:31
 * @parameter E-mail:15895982509@163.com
 */
public interface RoleResourceService {


    /**
     * 插入一个使用者使用的应用
     *
     * @param role
     * @param resource
     * @return int
     */
    public int insert(Role role, Resource resource);

    /**
     * 通过role和resource删除关联
     *
     * @param role
     * @param resource
     * @return int
     */
    public int delete(Role role, Resource resource);

    /**
     * 更新角色资源权限数据
     *
     * @param oldRole
     * @param oldResource
     * @param newRole
     * @param newResouce
     * @return int
     */
    public int update(Role oldRole, Resource oldResource, Role newRole, Resource newResouce);

    /**
     * 通过roleId查找所有应用
     *
     * @param roleId
     * @return List< ResourceModel >
     */
    public List<Resource> getResourceListByRoleId(String roleId);

    /**
     * 通过resourceId查找所有角色
     *
     * @param resourceId
     * @return List< RoleModel >
     */
    public List<Role> getRoleListByResourceId(String resourceId);

    /**
     * 查看数据库中是否存在此记录
     *
     * @param role
     * @param resource
     * @return boolean
     */
    public boolean isRoleResourceExist(Role role, Resource resource);

    /**
     * 通过roleId删除下面的所有资源权限
     *
     * @param roleId
     * @return int
     */
    public int deleteAllRoleResourceByRoleId(String roleId);

}
