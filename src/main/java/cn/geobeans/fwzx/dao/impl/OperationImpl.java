package cn.geobeans.fwzx.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.fwzx.dao.OperationDao;
import cn.geobeans.fwzx.model.Operation;
import com.leo.util.StringUtil;
import com.leo.util.calendar.CalendarUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
@Repository("operationDao")
public class OperationImpl extends BaseDaoImpl<Operation> implements OperationDao {

    /**
     * 获取某个情况下的某个应用被访问的次数
     *
     * @param projectId     应用的id
     * @param result        操作的结果
     * @param operationTime 操作的时间
     * @return 返回结果的数量
     */
    @Override
    public int getCount(String projectId, String result, Date operationTime) {
        String hql = "select operation from Operation operation where 1=1";
        if (!StringUtil.isNull(projectId)) {
            hql += " and operation.id='" + projectId + "'";
        }
        if (!StringUtil.isNull(result)) {
            hql += " and operation.result='" + result + "'";
        }
        if (operationTime != null) {
            hql += " and operation.operateTime=" + operationTime;
        }
        return super.list(hql).size();
    }

    /**
     * 通过条件获取操作结果
     *
     * @param result    操作的结果
     * @param projectId 应用id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回Operation的列表
     */
    @Override
    public List<Operation> getListByParams(String result, String projectId, Date startTime, Date endTime) {

        String hql = "select operation from Operation operation where 1=1";
        if (!StringUtil.isNull(result)) {
            hql += " and operation.result='" + result + "'";
        }
        if (!StringUtil.isNull(projectId)) {
            hql += " and operation.id='" + projectId + "'";
        }
        if (startTime != null) {
            if (endTime == null) {
                endTime = CalendarUtil.getDateTime();
            }
            hql += " and operation.operateTime between " + startTime + " and " + endTime;
        }
        return super.list(hql);
    }
}
