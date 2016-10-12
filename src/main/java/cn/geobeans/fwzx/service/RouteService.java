package cn.geobeans.fwzx.service;

import java.util.List;
import java.util.Map;

import cn.geobeans.fwzx.model.RouteModel;

/**
 *@author liuxi E-mail:15895982509@163.com
 *@version 创建时间:2016-3-28下午2:25:33
 */
public interface RouteService {
	
	/**
	 * 插入一个外部接口对象
	 * @param model
	 * @return 
	 * */
	public int insert(RouteModel model);
	
	/**
	 * 通过删除一个接口对象
	 * @param id
	 * @return 
	 * */
	public int delete(String id);
	
	/**
	 * 通过id查找一个对象
	 * @param id
	 * @return RouteModel
	 * */	
	public RouteModel get(String id);
	
	/**
	 * 更新操作对象
	 * @param model
	 * @return int
	 * */
	public int update(RouteModel model);

	/**
	 * 通过serverName来确定一个对象
	 *
	 * */
	public RouteModel getByServerName(String serverName);
	/**
	 * 查找所有的对象
	 * @return List < RouteModel >
	 * */
	public List<RouteModel> findList();
	
	
	/**
	 * 查找包含服务名和应用名的对象
	 * */
	public List<Map<String, Object>> getListByNameOrProjectName(String name,String projectId);
}
