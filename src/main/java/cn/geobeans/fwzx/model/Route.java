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
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-28下午2:27:57
 */

/**
 * 路由
 */
@Entity
@Table(name = "fwzx_route")
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 路由的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 所在的应用
     */
    private Project project;

    /**
     * 服务的接口名
     */
    private String serverName;

    /**
     * 所影射的远程路径
     */
    private String serverAddr;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口返回数据类型
     */
    private String dataReturnType;

    /**
     * 服务注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date regTime = CalendarUtil.getTime();


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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")/*这里不能定义被关联的列*/
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "server_name")
    @NotEmpty(message = "接口的名称不能为空")
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Column(name = "server_addr")
    @NotEmpty(message = "接口的名称不能为空")
    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "data_return_type")
    public String getDataReturnType() {
        return dataReturnType;
    }

    public void setDataReturnType(String dataReturnType) {
        this.dataReturnType = dataReturnType;
    }

    @Column(name = "reg_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}