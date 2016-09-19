package cn.geobeans.fwzx.model;

import java.util.List;

import cn.geobeans.common.model.IdModel;
import cn.geobeans.common.util.CalendarUtil;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-29下午7:37:27
 */

/**
 * 服务
 * */
public class ProjectModel extends IdModel {

    /**
     * 服务的ip
     * */
    private String ip;

    /**
     * 服务项目名
     * */
    private String name;

    /**
     * 服务端口号
     * */
    private String port;

    /**
     * 服务的状态
     * normal(正常), disconnect(未连接)
     * */
    private String state;


    /**
     * 服务描述
     * */
    private String description;

    /**
     * 应用注册时间
     * */
    private String regTime = CalendarUtil.getDataTimeStr();

    /**
     * 服务提供者
     * */
    private String provider;

    /**
     * 使用者
     * */
    private List<UsageModel> usageList;

    /**
     * 文件名
     * */
    private String fileName;

    /**
     * 文件路径
     * */
    private String filePath;

    /**
     * 核查状态
     * */
    private String checkState;

    /**
     * 测试页面
     * */
    private String testUrl;

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<UsageModel> getUsageList() {
        return usageList;
    }

    public void setUsageList(List<UsageModel> usageList) {
        this.usageList = usageList;
    }

}