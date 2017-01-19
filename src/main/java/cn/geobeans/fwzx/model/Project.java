package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-29下午7:37:27
 */

/**
 * 服务
 */
@Entity
@Table(name = "fwzx_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 服务的ip
     */
    private String ip;

    /**
     * 服务项目名
     */
    private String name;

    /**
     * 服务端口号
     */
    private Integer port;

    /**
     * 服务的状态
     * normal(正常), disconnect(未连接)
     */
    private String state;


    /**
     * 服务描述
     */
    private String description;

    /**
     * 应用注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date regTime = CalendarUtil.getTime();

    /**
     * 服务提供者
     */
    private String provider;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 核查状态
     */
    private String checkState;

    /**
     * 测试页面
     */
    private String testUrl;


    /**
     * 使用者列表
     */
    private List<Usage> usageList;

    /**
     * 路由列表
     */
    private List<Route> routeList;

    /**
     * 空构造方法
     */
    public Project() {
    }

    public Project(String ip, String name, Integer port, String description, String provider, String testUrl) {
        this.ip = ip;
        this.name = name;
        this.port = port;
        this.description = description;
        this.provider = provider;
        this.testUrl = testUrl;
    }

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column(name = "ip")
    @NotEmpty(message = "应用ip不能为空")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "name", unique = true)
    @NotEmpty(message = "应用名称不能为空")
    public String getName() {//unique 定义唯一约束，即不允许应用名重复
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "port")
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "reg_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    @Column(name = "provider")
    @NotEmpty(message = "应用的提供方不能为空")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "check_state")
    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    @Column(name = "test_url")
    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "fwzx_usage_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "usage_id")})
    private List<Usage> getUsageList() {
        return usageList;
    }

    public void setUsageList(List<Usage> usageList) {
        this.usageList = usageList;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", port=" + port +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", regTime=" + regTime +
                ", provider='" + provider + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", checkState='" + checkState + '\'' +
                ", testUrl='" + testUrl + '\'' +
                '}';
    }
}