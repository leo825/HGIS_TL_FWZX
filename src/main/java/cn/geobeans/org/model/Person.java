package cn.geobeans.org.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 人员对象,对于人员而言，某些项目中，可能Person里面会有一些非常特殊的属性，并不能够在人员对象中
 * 一一展示，如学校系统：教师和学生要存储的信息完全不一致，此时就创建两个关联类来和Person进行关联
 * 然后根据情况来确定是否有必要提供添加人员的功能，如果都是使用其他对象关联，如果所有的人员要么是教师
 * 要么是学生，此时可以提供相应的教师和学生的添加功能，在添加学生和教师的时候直接添加Person对象
 * 所以此处就把的公共属性抽取出来
 */
@Entity
@Table(name = "fwzx_person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 人员唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 人员姓名
     */
    private String name;
    /**
     * 人员身份证号
     */
    private String sfzh;
    /**
     * 人员联系电话
     */
    private String phone;
    /**
     * 人员性别：女：0，男：1
     */
    private Integer sex;

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

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
