package cn.geobeans.fwzx.model;

import cn.geobeans.common.util.ProjectUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by LX on 2016/12/28.
 */

@Entity
@Table(name = "FWZX_USAGE_PROJECT")
public class UsageProject implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 中间表的唯一识别
     */
    private String id = ProjectUtil.getUuid();
    /**
     * usage用户的id
     */
    private String usageId;
    /**
     * project所对应的id
     */
    private String projectId;


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

    @Column(name = "USAGE_ID")
    @NotEmpty(message = "中间表中usageId不能为空")
    public String getUsageId() {
        return usageId;
    }

    public void setUsageId(String usageId) {
        this.usageId = usageId;
    }

    @Column(name = "PROJECT_ID")
    @NotEmpty(message = "中间表中projectId不能为空")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
