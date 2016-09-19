package cn.geobeans.fwzx.service;

import java.util.List;
import java.util.Map;

import cn.geobeans.fwzx.model.ProjectModel;

/**
 *@author liuxi E-mail:15895982509@163.com
 *@version 创建时间:2016-3-29下午8:03:30
 */
public interface ProjectService {
	
	/**
	 * 插入一个外部应用对象
	 * @param model
	 * @return 
	 * */
	public int insert(ProjectModel model);
	
	/**
	 * 通过删除一个应用对象
	 * @param id
	 * @return 
	 * */
	public int delete(String id);
	
	/**
	 * 通过id查找一个对象
	 * @param id
	 * @return ProgramModel
	 * */	
	public ProjectModel get(String id);
	
	/**
	 * 更新操作对象
	 * @param model
	 * @return int
	 * */
	public int update(ProjectModel model);
	
	/**
	 * 查找所有的对象
	 * @return List < ProgramModel >
	 * */
	public List<ProjectModel> findList();
	
	/**
	 * 通过name和provider获取所有符合的应用
	 * @return List < ProjectModel >
	 * */
	public List<Map<String, Object>> getListByNameOrProvider(String name, String provider, String userId);
	
	
	/**
	 * 获取所有的提供者
	 * @return List
	 * 
	 * */
	public List<String> getAllProviders();
	
	/**
	 * 通过应用名来获取应用
	 * 
	 * */
	public ProjectModel getProjectByName(String name);
	
	/**
	 * 统计每个月提供应用的数量
	 * 
	 * */
	public int getProjectCountByMonth(String yearMonth);
	
	/**
	 * 统计每个月提供者数量
	 * */
	public int getProviderCountByMonth(String yearMonth);
	
	/**
	 * 某年某个用户提供应用比例
	 * */
	public float getPercentOfProjectByYear(String year,String provider);
	
	/**
	 * 获取某一年的提供者
	 * */
	public List<String> getProvidersByYear(String year);
	
	/**
	 * 通过名称获取应用被使用的百分比
	 * */
	public float getPercentOfUsages(String projectId);
	
	/**
	 * 更新应用状态
	 * */
	public int updataProjectState(ProjectModel project);
}
