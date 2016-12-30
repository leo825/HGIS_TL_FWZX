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
 * @version 创建时间:2016-5-16下午6:26:53
 */

/**
 * 资源权限
 */

@Entity
@Table(name = "FWZX_RESOURCE")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 资源的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 父资源id
     */
    private Resource parent;

    /**
     * 资源名称(唯一)
     */
    private String name;

    /**
     * 资源别名
     */
    private String nickname;

    /**
     * 资源描述
     */
    private String description;


    /**
     * 资源存在于的角色
     */
    private List<Role> roleList;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    @Column(name = "NAME")
    @NotEmpty(message = "资源的名称不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NICKNAME")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "FWZX_ROLE_RESOURCE",
            joinColumns = {@JoinColumn(name = "RESOURCE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            if (this.getClass() == obj.getClass()) {
                Resource u = (Resource) obj;
                if (this.getId().equals(u.getId())) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
    }

}
