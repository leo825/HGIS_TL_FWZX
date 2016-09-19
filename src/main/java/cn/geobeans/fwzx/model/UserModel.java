package cn.geobeans.fwzx.model;

import java.util.List;

import cn.geobeans.common.model.IdModel;
import cn.geobeans.common.util.CalendarUtil;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-5-16下午6:10:53
 */

/**
 * 用户
 */
public class UserModel extends IdModel {

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
    private String registerTime = CalendarUtil.getDataTimeStr();

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
    private List<RoleModel> roleList;

    public List<RoleModel> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleModel> roleList) {
        this.roleList = roleList;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
