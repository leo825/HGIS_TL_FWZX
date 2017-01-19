package cn.geobeans.fwzx.service;

import cn.geobeans.fwzx.model.Project;

import java.util.List;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-29下午8:03:30
 */
public interface ProjectService {

    /**
     * 添加一个应用
     *
     * @param model 一个应用实例
     */
    public void add(Project model);


    /**
     * 通过应用id删除一个应用
     *
     * @param id 应用id
     */
    public void delete(String id);

    /**
     * 通过id获取一个应用实例
     *
     * @param id 应用id
     * @return 返回一个应用实例
     */
    public Project load(String id);

    /**
     * 更新一个应用
     * @param model 更新的应用
     */
    public void update(Project model);


    /**
     * 获取所有的应用
     * @return 返回一个应用列表
     */
    public List<Project> findList();

    /**
     * 根据过滤条件name和provider获取所有符合的应用
     * @param name 应用名称
     * @param provider 应用提供者
     * @return 返回应用符合条件的应用的列表
     */

    public List<Project> getListByParams(String name, String provider);


    /**
     * 获取所有的提供者
     *
     * @return 返回应用提供者列表
     */
    public List<String> getAllProviders();


    /**
     * 统计某年某月提供的应用数量
     * @param year 某一年
     * @param month 某一月
     * @return 返回某年某月应用的的数量
     */
    public int getProjectCountByYearMonth(int year,int month);

    /**
     * 统计某年某月提供者数量
     * @param year 某一年
     * @param month 某一月
     * @return 返回某年某月提供者数量
     */
    public int getProviderCountByYearMonth(int year, int month);

    /**
     * 某年某个用户提供应用所占的比例
     * @param year 某一年
     * @param provider 应用提供者
     * @return 返回某个提供者提供应用所占的百分比
     */
    public float getPercentOfProjectByYear(int year, String provider);

    /**
     * 获取某一年的提供者
     * @param year 某一年
     * @return 返回提供者的列表
     */
    public List<String> getProvidersByYear(int year);

    /**
     * 通过应用id获取应用被使用者使用的百分比
     * @param projectId 应用的id
     * @return
     */
    public float getPercentOfUsages(String projectId);

}
