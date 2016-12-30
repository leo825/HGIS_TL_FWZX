package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by LX on 2016/12/29.
 */
@Entity
@Table(name = "FWZX_ROLE_RESOURCE")
public class RoleResource implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 关系表的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 资源id
     */
    private String resourceId;


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

    @Column(name = "ROLE_ID")
    @NotEmpty(message = "中间表中roleId不能为空")
    public String getRoleId() {
        return roleId;
    }


    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "RESOURCE_ID")
    @NotEmpty(message = "中间表中resourceId不能为空")
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
