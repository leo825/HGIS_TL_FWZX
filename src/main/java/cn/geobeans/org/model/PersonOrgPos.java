package cn.geobeans.org.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 人员组织岗位对应关系表，这张表里面存储了人员和组织和岗位的对应关系
 */
@Entity
@Table(name = "fwzx_person_org_pos")
public class PersonOrgPos implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 关系的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 人员id，这里也可以存储实体类，但是不太建议
     */
    private String personId;
    /**
     * 组织的id
     */
    private String orgId;
    /**
     * 岗位的id
     */
    private String posId;

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

    @Column(name = "person_id")
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Column(name = "org_id")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Column(name = "pos_id")
    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }
}
