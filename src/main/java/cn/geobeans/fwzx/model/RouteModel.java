package cn.geobeans.fwzx.model;

import cn.geobeans.common.model.IdModel;
import cn.geobeans.common.util.CalendarUtil;

/**
 *@author liuxi E-mail:15895982509@163.com
 *@version 创建时间:2016-3-28下午2:27:57
 */

/**
 * 路由
 * */

public class RouteModel extends IdModel{

	/**
     * 所在的工程id
     * */
	private String projectId;
	
	/**
	 * 服务的接口名
	 * */
	private String serverName;
	
	/**
	 * 所影射的远程路径
	 * */
	private String serverAddr;
	
	/**
	 * 接口描述
	 * */
	private String description;
	
	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	/**
	 * 服务注册时间
	 * */
	private String regTime = CalendarUtil.getDataTimeStr();
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	/**
	 * 接口返回数据类型
	 * */
    private String dataReturnType;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}



	public String getDataReturnType() {
		return dataReturnType;
	}

	public void setDataReturnType(String dataReturnType) {
		this.dataReturnType = dataReturnType;
	}


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}