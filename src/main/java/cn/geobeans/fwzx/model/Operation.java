package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *@author liuxi
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-25下午6:25:26
 */

/**
 * 操作日志
 */

@Entity
@Table(name = "FWZX_OPERATION")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 操作的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 操作ip
     */
    private String ip;

    /**
     * 操作的服务
     */
    private String serverName;

    /**
     * 服务名称
     */
    private String projectName;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 操作时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date operateTime = CalendarUtil.getTime();

    /**
     * ip归属
     */
    private String userName;

    /**
     * 操作描述
     */
    private String description;


    public Operation() {
    }

    public Operation(String ip, String serverName, String projectName, String result, String userName, String description) {
        this.ip = ip;
        this.serverName = serverName;
        this.projectName = projectName;
        this.result = result;
        this.userName = userName;
        this.description = description;
    }

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "IP")
    @NotEmpty(message = "操作的ip不能为空")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "SERVER_NAME")
    @NotEmpty(message = "操作的接口名称不能为空")
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Column(name = "PROJECT_NAME")
    @NotEmpty(message = "操作的服务名称不能为空")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "RESULT")
    @NotEmpty(message = "操作的结果不能为空")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Column(name = "OPERATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Column(name = "USER_NAME")
    @NotEmpty(message = "操作ip的归属不能为空")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}