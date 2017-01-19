package cn.geobeans.fwzx.dao;

import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.fwzx.model.Operation;

import java.util.Date;
import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
public interface OperationDao extends BaseDao<Operation> {

    /**
     * 获取某个情况下的某个应用被访问的次数
     *
     * @param projectId     应用的id
     * @param result        操作的结果
     * @param operationTime 操作的时间
     * @return 返回结果的数量
     */
    public int getCount(String projectId, String result, Date operationTime);

    /**
     * 通过条件获取操作结果
     *
     * @param result    操作的结果
     * @param projectId 应用id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回Operation的列表
     */
    public List<Operation> getListByParams(String result, String projectId, Date startTime, Date endTime);


}
