package cn.geobeans.org.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 组织机构类型对象，用来设定系统中存在哪些组织类型
 * 如：对于学校而言，可能存在：学校，分校，校长办，行政部门，教学部门，专业，班级
 *
 */
@Entity
@Table(name = "fwzx_org_type")
public class OrgType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 组织机构类型的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 类型的名称
     */
    private String name;
    /**
     * 类型的sn序号
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

    @NotEmpty(message = "组织机构类型名称不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "组织机构标识不能为空")
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
