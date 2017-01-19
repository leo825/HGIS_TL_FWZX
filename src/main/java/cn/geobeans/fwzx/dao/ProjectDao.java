package cn.geobeans.fwzx.dao;


import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.fwzx.model.Project;

import java.util.List;

/**
 * Created by LX on 2016/12/29.
 */

public interface ProjectDao extends BaseDao<Project> {

    /**
     * 根据过滤条件name和provider获取所有符合的应用
     *
     * @param name     应用的名称
     * @param provider 应用的提供者
     * @return 返回符合过滤条件的应用的List
     */
    public List<Project> getListByParams(String name, String provider);

    /**
     * 获取所有的应用提供者
     *
     * @return 返回所有应用提供者的List
     */
    public List<String> getAllProviders();

    /**
     * 精确获取某个时间的某个提供者提供的所有应用
     *
     * @param year     某一年
     * @param month    某一月,0表示这个参数为空
     * @param provider 某一个提供者
     * @return 返回符合条件的应用列表
     */
    public List<Project> getProjectsByParams(int year, int month, String provider);


}
