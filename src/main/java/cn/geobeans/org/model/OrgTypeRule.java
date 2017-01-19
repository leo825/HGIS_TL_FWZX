package cn.geobeans.org.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 组织机构类型的规则表，用来确定组织之间的关系
 *
 */
@Entity
@Table(name = "fwzx_org_type_rule")
public class OrgTypeRule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 组织机构类型规则的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 规则的父id
     */
    private String pid;
    /**
     * 规则的子id
     */
    private String cid;
    /**
     * 两者之间的数量，如果为-1表示可以有无限多个
     */
    private Integer num;

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
