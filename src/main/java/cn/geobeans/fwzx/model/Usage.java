package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-4-25下午6:22:25
 */

/**
 * 调用者
 */
@Entity
@Table(name = "fwzx_usage")
public class Usage implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 使用者的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 调用服务的调用者的ip
     */
    private String ip;

    /**
     * 调用服务的调用者的名称
     */
    private String name;

    /**
     * 调用者描述
     */
    private String description;

    /**
     * 调用者所使用的应用
     */
    private List<Project> projectList;

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


    @Column(name = "ip",unique = true)
    @NotEmpty(message = "使用者ip不能为空")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "name")
    @NotEmpty(message = "使用者归属不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "fwzx_usage_project",
            joinColumns = {@JoinColumn(name = "usage_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")})
    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
