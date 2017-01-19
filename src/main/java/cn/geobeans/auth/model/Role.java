package cn.geobeans.auth.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色对象
 */
@Entity
@Table(name = "fwzx_role")
public class Role implements Principal, Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PRINCIPAL_TYPE = "role";
    /**
     * 权限的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 角色的名称
     */
    private String name;

    /**
     * 角色的snma码
     */
    private String sn;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
