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
@Table(name = "FWZX_USER_ROLE")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 中间表的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;

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

    @Column(name = "USER_ID")
    @NotEmpty(message = "用户的userId不能为空")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "ROLE_ID")
    @NotEmpty(message = "角色的roleId不能为空")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
