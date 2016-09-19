package cn.geobeans.fwzx.model;

import java.util.List;

import cn.geobeans.common.model.IdModel;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-16下午6:25:35
 */

/**
 * 角色
 * */

public class RoleModel extends IdModel{
	
	/**
	 * 角色名称
	 * */
	private String name;
	
	/**
	 * 角色描述
	 * */
	private String description;
	
	/**
	 * 角色所属权限
	 * */
	private List<ResourceModel> resouceList;

	
	public List<ResourceModel> getResouceList() {
		return resouceList;
	}

	public void setResouceList(List<ResourceModel> resouceList) {
		this.resouceList = resouceList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    @Override  
    public boolean equals(Object obj){  
        if(obj == null){  
            return false;  
        }else {           
                if(this.getClass() == obj.getClass()){  
                    RoleModel u = (RoleModel) obj;
                    if(this.getId().equals(u.getId())){  
                        return true;  
                    }else{  
                        return false;  
                    }  
                  
            }else{  
                return false;  
            }  
        }             
    }
}
