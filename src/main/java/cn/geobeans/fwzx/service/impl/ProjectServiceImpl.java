package cn.geobeans.fwzx.service.impl;

import cn.geobeans.fwzx.dao.ProjectDao;
import cn.geobeans.fwzx.dao.UsageProjectDao;
import cn.geobeans.fwzx.model.Project;
import cn.geobeans.fwzx.model.UsageProject;
import cn.geobeans.fwzx.service.ProjectService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-29下午8:08:59
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    private static Logger logger = Logger.getLogger(ProjectServiceImpl.class);

    @Inject
    private ProjectDao projectDao;
    @Inject
    private UsageProjectDao usageProjectDao;




    /**
     * 添加一个应用
     *
     * @param model 一个应用实例
     */
    @Override
    public void add(Project model) {
        projectDao.add(model);
    }

    /**
     * 通过应用id删除一个应用
     *
     * @param id 应用id
     */
    @Override
    public void delete(String id) {
        projectDao.delete(id);
    }

    /**
     * 通过id获取一个应用实例
     *
     * @param id 应用id
     * @return 返回一个应用实例
     */
    @Override
    public Project load(String id) {
        return projectDao.load(id);
    }

    /**
     * 更新一个应用
     *
     * @param model 更新的应用
     */
    @Override
    public void update(Project model) {
        projectDao.update(model);
    }

    /**
     * 获取所有的应用
     *
     * @return 返回一个应用列表
     */
    @Override
    public List<Project> findList() {
        return projectDao.getListByParams("", "");
    }

    /**
     * 根据过滤条件name和provider获取所有符合的应用
     *
     * @param name     应用名称
     * @param provider 应用提供者
     * @return 返回应用符合条件的应用的列表
     */
    @Override
    public List<Project> getListByParams(String name, String provider) {
        return projectDao.getListByParams(name, provider);
    }

    /**
     * 获取所有的提供者
     *
     * @return 返回应用提供者列表
     */
    @Override
    public List<String> getAllProviders() {
        return projectDao.getAllProviders();
    }

    /**
     * 统计某年某月提供的应用数量
     *
     * @param year  某一年
     * @param month 某一月
     * @return 返回某年某月应用的的数量
     */
    @Override
    public int getProjectCountByYearMonth(int year, int month) {
        return 0;
    }

    /**
     * 统计某年某月提供者数量
     *
     * @param year  某一年
     * @param month 某一月
     * @return 返回某年某月提供者数量
     */
    @Override
    public int getProviderCountByYearMonth(int year, int month) {

        List<Project> projectList = projectDao.getProjectsByParams(year, month, "");
        return projectList.size();
    }

    /**
     * 某年某个用户提供应用所占的比例
     *
     * @param year     某一年
     * @param provider 应用提供者
     * @return 返回某个提供者提供应用所占的百分比
     */
    @Override
    public float getPercentOfProjectByYear(int year, String provider) {
        List<Project> allProjectList = projectDao.getProjectsByParams(year, 0, "");
        List<Project> providerProjectList = projectDao.getProjectsByParams(year, 0, provider);

        if (allProjectList.size() == 0 || allProjectList.size() < providerProjectList.size()) {
            return 0;
        } else {
            float result = (float) providerProjectList.size() / allProjectList.size();
            return result * 100;
        }
    }

    /**
     * 获取某一年的提供者
     *
     * @param year 某一年
     * @return 返回提供者的列表
     */
    @Override
    public List<String> getProvidersByYear(int year) {
        List<Project> projectList = projectDao.getProjectsByParams(year, 0, "");
        List<String> providerList = new ArrayList<String>();
        for(Project project:projectList){
            if(!providerList.contains(project.getProvider())){
                providerList.add(project.getProvider());
            }
        }
        return providerList;
    }

    /**
     * 通过应用id获取应用被使用者使用的百分比
     *
     * @param projectId 应用的id
     * @return
     */
    @Override
    public float getPercentOfUsages(String projectId) {
        List<UsageProject> allUsageProjectList = usageProjectDao.getListByParams("","");
        List<UsageProject> usageProjectList = usageProjectDao.getListByParams("",projectId);

        if (allUsageProjectList.size() == 0 || allUsageProjectList.size() < usageProjectList.size()) {
            return 0;
        } else {
            float result = (float) usageProjectList.size() / allUsageProjectList.size();
            return result * 100;
        }
    }
}
