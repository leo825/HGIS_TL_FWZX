package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-5-16下午6:10:53
 */

/**
 * 用户
 */
@Entity
@Table(name = "FWZX_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * 账户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date registerTime = CalendarUtil.getTime();

    /**
     * 用户状态
     */
    private String accountState;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * email
     */
    private String email;

    /**
     * 角色列表
     */
    private List<Role> roleList;

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

    @Column(name = "ACCOUNT")
    @NotEmpty(message = "用户的账号不能为空")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "PASSWORD")
    @NotEmpty(message = "用户的密码不能为空")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "NICKNAME")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "REGISTERTIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getAccountState() {
        return accountState;
    }

    @Column(name = "ACCOUNTSTATE")
    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    @Column(name = "TELEPHONE")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "FWZX_USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
    )
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
