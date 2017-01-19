package cn.geobeans.auth.model;


import cn.geobeans.common.model.SysException;
import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 权限控制的关键类，这个类用来存储主体和资源之间的关系，用来确定主体能够对哪些资源进行哪些操作
 */
@Entity
@Table(name = "fwzx_acl")
public class Acl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限的唯一识别
     */
    private String id = ProjectUtil.getUuid();

    /**
     * 主体的类型
     */
    private String ptype;

    /**
     * 资源的类型
     */
    private String rtype;

    /**
     * 主体的id
     */
    private String pid;

    /**
     * 资源的id
     */
    private String rid;

    /**
     * 操作的状态，32位数据库中存储的是11位数字，每一位代表一个操作
     */
    private Integer aclState;

    public void setMenuType() {
        this.rtype = MenuResources.RES_TYPE;
    }

    public void setControllerType() {
        this.rtype = ControllerResources.RES_TYPE;
    }

    public void setUserType() {
        this.ptype = User.PRINCIPAL_TYPE;
    }

    public void setRoleType() {
        this.ptype = Role.PRINCIPAL_TYPE;
    }

    /**
     * 设置权限，在某个位置设置访问或者无法访问
     *
     * @param index
     * @param permit
     */
    public void setPermission(Integer index, boolean permit) {
        if (index < 0 || index > 31) throw new SysException("权限的位置只能在0-31之间");
        this.aclState = setBit(this.aclState, index, permit);
    }

    /**
     * 具体进行设置
     *
     * @param state
     * @param index
     * @param permit
     */
    public int setBit(int state, int index, boolean permit) {
        int tmp = 1;
        tmp = tmp << index;
        if (permit) {
            state = state | tmp;
        } else {
            tmp = ~tmp;
            state = state & tmp;
        }
        return state;
    }

    /**
     * 检查在某个位置是否可以访问
     *
     * @param index
     * @return
     */
    public boolean checkPermission(int index) {
        int tmp = 1;
        tmp = tmp << index;
        int num = this.aclState & tmp;
        return num > 0;
    }

    public static boolean checkPermission(int index, int aclState) {
        int tmp = 1;
        tmp = tmp << index;
        int num = aclState & tmp;
        return num > 0;
    }


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
     * 主体类型
     *
     * @return
     */
    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    /**
     * 资源的类型
     *
     * @return
     */
    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    /**
     * 主体的id
     *
     * @return
     */
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * 资源的id
     *
     * @return
     */
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * 对方法的操作状态，存储的是一个4个字节的整数，其实就可以存储0-31位的操作
     * 000000....1011-->可以进行CREATE,READ,DELETE,无法进行UPDATE
     * 数据库中存储的值是11
     *
     * @return
     */
    @Column(name = "acl_state")
    public Integer getAclState() {
        return aclState;
    }

    public void setAclState(Integer aclState) {
        this.aclState = aclState;
    }
}
