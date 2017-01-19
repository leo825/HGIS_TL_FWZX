package cn.geobeans.fwzx.dao;

import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.fwzx.model.UsageProject;

import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
public interface UsageProjectDao extends BaseDao<UsageProject> {

    /**
     * 通过使用者id或者应用id来获取中间表实体个数
     *
     * @param usageId   使用者id
     * @param projectId 应用id
     * @return 返回使用者应用的实例列表
     */
    public List<UsageProject> getListByParams(String usageId, String projectId);
}
