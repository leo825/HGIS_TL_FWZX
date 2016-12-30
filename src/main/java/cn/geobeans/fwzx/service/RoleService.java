package cn.geobeans.fwzx.service;

import java.util.List;

import cn.geobeans.fwzx.model.Resource;
import cn.geobeans.fwzx.model.Role;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-20下午3:52:12
 */
public interface RoleService {

	/**
	 * 插入一个使用者对象
	 * @param model
	 * @return 
	 * */
	public int insert(Role model);
	
	/**
	 * 通过删除一个使用者对象
	 * @param id
	 * @return 
	 * */
	public int delete(String id);
	
	/**
	 * 通过id查找一个对象
	 * @param id
	 * @return RoleModel
	 * */	
	public Role get(String id);
	
	/**
	 * 更新操作对象
	 * @param model
	 * @return int
	 * */
	public int update(Role model);
	
	
	/**
	 * 查找所有的对象
	 * @return List < RoleModel >
	 * */
	public List<Role> findList();
	
	/**
	 * 根据角色名查找对象
	 * @param roleName
	 * @return RoleModel
	 * */
	public Role getRoleByName(String roleName);
	
	/**
	 * 通过角色id获取
	 * @param roleId
	 * @retuan List< ResourceModel >
	 * */
	public List<Resource> getResourceByRoleId(String roleId);
	
	/**
	 * 通过名称和描述获取列表
	 * @param name
	 * @param description
	 * @retrun List< RoleModel >
	 * */
	public List<Role> getListByNameAndDescription(String name, String description);
}
