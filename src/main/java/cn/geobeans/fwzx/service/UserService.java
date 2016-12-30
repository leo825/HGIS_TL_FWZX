package cn.geobeans.fwzx.service;

import cn.geobeans.fwzx.model.Resource;
import cn.geobeans.fwzx.model.Role;
import cn.geobeans.fwzx.model.User;

import java.util.List;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-20下午3:51:44
 */
public interface UserService {
	/**
	 * 插入一个使用者对象
	 * @param model
	 * @return 
	 * */
	public int insert(User model);
	
	/**
	 * 通过删除一个使用者对象
	 * @param id
	 * @return 
	 * */
	public int delete(String id);
	
	/**
	 * 通过id查找一个对象
	 * @param id
	 * @return UserModel
	 * */	
	public User get(String id);
	
	/**
	 * 更新操作对象
	 * @param model
	 * @return int
	 * */
	public int update(User model);
	
	
	/**
	 * 查找所有的对象
	 * @return List < UserModel >
	 * */
	public List<User> findList();

	/**
	 * 通过账户查找用户
	 * @param account
	 * @return UserModel
	 * */
	public User getUserByAccount(String account);
	
	
	/**
	 * 通过id获取用户的角色
	 * @param userId
	 * @return List< RoleModel >
	 * */
	public List<Role> getRoleListByUserId(String userId);
	
	/**
	 * 通过id获取权限
	 * @param userId
	 * @return List < ResourceModel >
	 * */
	public List<Resource> getResourceById(String userId);
	
	
	/**
	 * 通过账户或者昵称获取用户列表
	 * @param account
	 * @param nickname
	 * @return List< UserModel >
	 * */
	public List<User> getUsersByParams(String account,String nickname);
}
