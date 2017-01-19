package cn.geobeans.auth.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Controller资源的操作方法类，用来确定某个资源的操作所对应的方法
 */
@Entity
@Table(name = "fwzx_controller_oper")
public class ControllerOper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作方法的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 操作方法的sn码
     */
    private String sn;

    /**
     * 操作方法的方法名
     */
    private String methodName;

    /**
     * 操作方法的中文名称
     */
    private String name;

    /**
     * 操作方法在Acl中的位置
     */
    private Integer indexPos;

    /**
     * 资源id
     */
    private String rid;
    /*
     * 资源对象的sn
     */
    private String rsn;

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

    /**
     * 资源的标识，默认就通过ADD,DELETE,UPDATE,READ
     *
     * @return
     */
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 资源的方法，一个操作默认会对应多个方法add|addInput
     * 在初始化的时候，可以根据方法的名称来确定，如add开头就是ADD，其他没有声明的都是READ
     *
     * @return
     */
    @Column(name = "method_name")
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        if (this.methodName == null || "".equals(this.methodName)) {
            this.methodName = methodName;
        } else {
            if (this.methodName.indexOf(methodName) >= 0) {
                //原有的className已经包含了，就直接返回
                return;
            }
            this.methodName += "|" + methodName;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法的索引位置
     * 默认情况:0-->CREATE|ADD,1-->READ,2-->UPDATE,3-->DELETE其他，又开发人员手动指定
     *
     * @return
     */
    public Integer getIndexPos() {
        return indexPos;
    }

    public void setIndexPos(Integer indexPos) {
        this.indexPos = indexPos;
    }

    /**
     * 所对应的资源id
     *
     * @return
     */
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRsn() {
        return rsn;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }
}
