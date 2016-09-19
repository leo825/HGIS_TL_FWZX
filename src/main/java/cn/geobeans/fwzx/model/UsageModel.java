package cn.geobeans.fwzx.model;

import java.util.List;

import cn.geobeans.common.model.IdModel;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-4-25下午6:22:25
 */

/**
 * 调用者
 * */

public class UsageModel extends IdModel {

    /**
     * 调用服务的调用者的ip
     * */
    private String ip;

    /**
     * 调用服务的调用者的名称
     * */
    private String name;

    /**
     * 调用者描述
     * */
    private String description;

    /**
     * 调用者所使用的应用
     * */
    private List<ProjectModel> projectList;

    public List<ProjectModel> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectModel> projectList) {
        this.projectList = projectList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
