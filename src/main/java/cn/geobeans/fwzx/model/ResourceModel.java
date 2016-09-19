package cn.geobeans.fwzx.model;

import cn.geobeans.common.model.IdModel;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-5-16下午6:26:53
 */

/**
 * 资源权限
 * */
public class ResourceModel extends IdModel {

    /**
     * 父资源id
     * */
    private String parentId;

    /**
     * 资源名称(唯一)
     * */
    private String name;

    /**
     * 资源别名
     * */
    private String nickname;

    /**
     * 资源描述
     * */
    private String description;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            if (this.getClass() == obj.getClass()) {
                ResourceModel u = (ResourceModel) obj;
                if (this.getId().equals(u.getId())) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
    }

}
