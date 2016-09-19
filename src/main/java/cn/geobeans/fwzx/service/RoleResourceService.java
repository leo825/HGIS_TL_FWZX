package cn.geobeans.fwzx.service;

import java.util.List;
import java.util.Map;


import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.RoleModel;

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
    public int insert(RoleModel role, ResourceModel resource);

    /**
     * 通过role和resource删除关联
     *
     * @param role
     * @param resource
     * @return int
     */
    public int delete(RoleModel role, ResourceModel resource);

    /**
     * 更新角色资源权限数据
     *
     * @param oldRole
     * @param oldResource
     * @param newRole
     * @param newResouce
     * @return int
     */
    public int update(RoleModel oldRole, ResourceModel oldResource, RoleModel newRole, ResourceModel newResouce);

    /**
     * 通过roleId查找所有应用
     *
     * @param roleId
     * @return List< ResourceModel >
     */
    public List<ResourceModel> getResourceListByRoleId(String roleId);

    /**
     * 通过resourceId查找所有角色
     *
     * @param resourceId
     * @return List< RoleModel >
     */
    public List<RoleModel> getRoleListByResourceId(String resourceId);

    /**
     * 查看数据库中是否存在此记录
     *
     * @param role
     * @param resource
     * @return boolean
     */
    public boolean isRoleResourceExist(RoleModel role, ResourceModel resource);

    /**
     * 通过roleId删除下面的所有资源权限
     *
     * @param roleId
     * @return int
     */
    public int deleteAllRoleResourceByRoleId(String roleId);

}
