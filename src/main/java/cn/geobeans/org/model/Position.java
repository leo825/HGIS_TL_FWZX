package cn.geobeans.org.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 岗位的对象，用来确定某个人员所属的岗位
 * 存储的就是岗位的名称
 * 副校长，校长，处长，副处长，科长，普通员工
 */
@Entity
@Table(name = "fwzx_position")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 岗位的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 岗位的名称
     */
    private String name;
    /**
     * 岗位的sn
     */
    private String sn;
    /**
     * 岗位的是否具备管理功能，目前意义不大
     */
    private Integer manager;

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

    @NotEmpty(message = "岗位名称不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "岗位的标识不能为空")
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }
}
