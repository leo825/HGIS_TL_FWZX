package cn.geobeans.fwzx.service;

import cn.geobeans.fwzx.model.Project;
import cn.geobeans.fwzx.model.Usage;

import java.util.List;

/**
 * @author liuxi
 * @version 创建时间:2016-5-26上午9:57:19
 * @parameter E-mail:15895982509@163.com
 */
public interface UsageProjectService {

    /**
     * 插入一个使用者使用的应用
     *
     * @param usage
     * @param project
     * @return int
     */
    public int insert(Usage usage, Project project);

    /**
     * 通过删除一个使用者应用对象
     *
     * @param usage
     * @param project
     * @return int
     */
    public int delete(Usage usage, Project project);

    /**
     * 更新使用者应用数据
     *
     * @return int
     */
    public int update(Usage oldUsage, Project oldProject, Usage newUsage, Project newProject);

    /**
     * 通过usageId查找所有应用
     *
     * @param usageId
     * @return List< ProjectModel >
     */
    public List<Project> getProjectListByUsageId(String usageId);

    /**
     * 通过usageId查找所有应用
     *
     * @param projectId
     * @return List< UsageModel >
     */
    public List<Usage> getUsageListByProjectId(String projectId);


    /**
     * 查看数据库中是否存在此记录
     *
     * @param usageId
     * @param projectId
     * @return boolean
     */
    public boolean isUsageProjectExist(String usageId, String projectId);

    /**
     * 通过参数获取所有UsagModel
     *
     * @param ip        使用者ip
     * @param name      使用者归属
     * @param projectId 应用的id
     * @return List< UsageModel >
     */
    public List<Usage> getUsageListByParams(String ip, String name, String projectId);

}
