package cn.geobeans.fwzx.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.fwzx.dao.UsageProjectDao;
import cn.geobeans.fwzx.model.UsageProject;
import com.leo.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
@Repository("usageProjectDao")
public class UsageProjectDaoImpl extends BaseDaoImpl<UsageProject> implements UsageProjectDao {

    /**
     * 通过使用者id或者应用id来获取中间表实体个数
     *
     * @param usageId   使用者id
     * @param projectId 应用id
     * @return 返回使用者应用的实例列表
     */
    @Override
    public List<UsageProject> getListByParams(String usageId, String projectId) {

        String hql = "select usageProject from UsageProject usageProject where 1=1";
        if (!StringUtil.isNull(usageId)) {
            hql += " and usageProject.usageId='" + usageId + "'";
        }
        if (!StringUtil.isNull(projectId)) {
            hql += " and usageProject.projectId='" + projectId + "'";
        }
        return super.list(hql);
    }
}
