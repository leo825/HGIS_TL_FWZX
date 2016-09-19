package cn.geobeans.fwzx.service;

import java.util.List;

import cn.geobeans.fwzx.model.UsageModel;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-5-1下午12:35:30
 */
public interface UsageService {

	/**
	 * 插入一个使用者对象
	 * 
	 * @param model
	 * @return
	 * */
	public int insert(UsageModel model);

	/**
	 * 通过删除一个使用者对象
	 * 
	 * @param id
	 * @return
	 * */
	public int delete(String id, String projectId);

	/**
	 * 通过id查找一个对象
	 * 
	 * @param id
	 * @return UsageModel
	 * */
	public UsageModel get(String id);

	/**
	 * 更新操作对象
	 * 
	 * @param model
	 * @return int
	 * */
	public int update(UsageModel model);

	/**
	 * 查找所有的对象
	 * 
	 * @return List < UsageModel >
	 * */
	public List<UsageModel> findList();

	/**
	 * 通过IP来获取
	 * @param ip
	 * @return UsageModel
	 **/
	public UsageModel getByIp(String ip);
	
	/**
	 * 通过ip或者name获取List
	 * @param ip
	 * @param name
	 * @return List < UsageModel >
	 * */
	public List<UsageModel> getListByParms(String ip,String name);

}
