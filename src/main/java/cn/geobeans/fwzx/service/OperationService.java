package cn.geobeans.fwzx.service;

import java.util.List;
import java.util.Map;

import cn.geobeans.fwzx.model.OperationModel;

/**
 * @author liuxi
 * @version 创建时间:2016-4-25下午11:23:34
 * @parameter E-mail:15895982509@163.com
 */
public interface OperationService {


    /**
     * 插入一个外部接口对象
     *
     * @param model
     * @return
     */
    public int insert(OperationModel model);

    /**
     * 通过删除一个接口对象
     *
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 通过id查找一个对象
     *
     * @param id
     * @return OperationModel
     */
    public OperationModel get(String id);

    /**
     * 查找所有的对象
     *
     * @return List < OperationModel >
     */
    public List<OperationModel> findList();

    /**
     * 获取服务访问总次数
     *
     * @return Long
     */
    public int getAllCount(String projectId);

    /**
     * 获取服务访问成功次数
     *
     * @return Long
     */
    public int getSuccessCount(String projectId, String result);

    /**
     * 通过projectId和operationTime来获取服务每秒钟的操作次数
     */
    public int getCountBySecondOfProject(String projectId, String operationTime);

    /**
     * 符合查询符合条件的列表
     */
    public List<Map<String, Object>> getListByParms(String result, String projectId, String startTime, String endTime);

    /**
     * 获取某个应用某个时刻的访问量
     */
    public int getCountOfProjectByTime(String projectId, String operateTime);

}
