package cn.geobeans.common.model;

import cn.geobeans.common.util.ProjectUtil;

import java.io.Serializable;

/**
 * 通用实体类
 * Created by ice on 2016/3/15.
 */
public class IdModel implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id = ProjectUtil.getUuid();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
