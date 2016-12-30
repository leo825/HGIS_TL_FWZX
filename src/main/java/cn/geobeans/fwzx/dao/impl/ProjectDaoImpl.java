package cn.geobeans.fwzx.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.fwzx.dao.ProjectDao;
import cn.geobeans.fwzx.model.Project;
import com.leo.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by LX on 2016/12/29.
 */

@Repository("projectDao")
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao{
    /**
     * 根据过滤条件name和provider获取所有符合的应用
     *
     * @param projectName     应用的名称
     * @param provider 应用的提供者
     * @return 返回符合过滤条件的应用的List
     */
    @Override
    public List<Project> getListByNameOrProvider(String projectName, String provider) {
        String hql = "select project from Project project where";
        if(!StringUtil.isNull(projectName)){
            hql += " project.name=?";
        }
        if(!StringUtil.isNull(provider)){
            hql += " project.provider=?";
        }
        return super.list(hql,projectName,provider);
    }

    /**
     * 获取所有的应用提供者
     *
     * @return 返回所有应用提供者的List
     */
    @Override
    public List<String> getAllProviders() {
        return null;
    }

    /**
     * 精确获取某个时间的所有应用
     *
     * @param date 时间，格式为：yyyy-MM-dd
     * @return 返回应用的List
     */
    @Override
    public List<Project> getProjectsByDate(Date date) {
        return null;
    }
}
