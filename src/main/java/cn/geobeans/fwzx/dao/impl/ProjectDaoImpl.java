package cn.geobeans.fwzx.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.fwzx.dao.ProjectDao;
import cn.geobeans.fwzx.model.Project;
import com.leo.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LX on 2016/12/29.
 */

@Repository("projectDao")
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao {
    /**
     * 根据过滤条件name和provider获取所有符合的应用
     *
     * @param name     应用的名称
     * @param provider 应用的提供者
     * @return 返回符合过滤条件的应用的List
     */
    @Override
    public List<Project> getListByParams(String name, String provider) {
        String hql = "select project from Project project where 1=1";
        if (!StringUtil.isNull(name)) {
            hql += " and project.name='" + name + "'";
        }
        if (!StringUtil.isNull(provider)) {
            hql += " and project.provider='" + provider + "'";
        }
        return super.list(hql);
    }

    /**
     * 获取所有的应用提供者
     *
     * @return 返回所有应用提供者的List
     */
    @Override
    public List<String> getAllProviders() {
        String hql = "select distinct project.provider from Project project";
        return super.listObj(hql);
    }

    /**
     * 精确获取某个时间的某个提供者提供的所有应用
     *
     * @param year     某一年
     * @param month    某一月,0表示这个参数为空
     * @param provider 某一个提供者
     * @return 返回符合条件的应用列表
     */
    @Override
    public List<Project> getProjectsByParams(int year, int month, String provider) {
        String hql = "select project from Project project where year(project.regTime)=?";
        if (month != 0) {
            hql += " and month(project.regTime)=" + month;
        }
        if (!StringUtil.isNull(provider)) {
            hql += " and project.provider='" + provider + "'";
        }
        return super.list(hql, year);
    }

}
