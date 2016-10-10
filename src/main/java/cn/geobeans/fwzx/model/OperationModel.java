package cn.geobeans.fwzx.model;

import cn.geobeans.common.model.IdModel;
import cn.geobeans.common.util.CalendarUtil;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-25下午6:25:26
 */

/**
 * 操作日志
 * */
public class OperationModel extends IdModel{;

	/**
	 * 操作ip
	 * */
	private String ip;
	
	/**
	 * 操作的服务
	 * */
	private String serverName;

	/**
	 * 服务名称
	 * */
	private String projectName;
	
	/**
	 * 操作结果
	 * */
	private String result;
	
	/**
	 * 操作时间
	 * */
	private String operateTime = CalendarUtil.getDataTimeStr();
	
	/**
	 * ip归属
	 * */
	private String userName;
	
	/**
	 * 操作描述
	 * */
	private String description;


	public OperationModel() {
	}

	public OperationModel(String ip, String serverName, String projectName, String result, String userName, String description) {
		this.ip = ip;
		this.serverName = serverName;
		this.projectName = projectName;
		this.result = result;
		this.userName = userName;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}




}