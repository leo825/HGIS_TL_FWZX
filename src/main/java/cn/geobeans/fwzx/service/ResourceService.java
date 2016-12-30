package cn.geobeans.fwzx.service;

import java.util.List;

import cn.geobeans.fwzx.model.Resource;

/**
 * @author liuxi
 * @version 创建时间:2016-5-22下午9:07:46
 * @parameter E-mail:15895982509@163.com
 */
public interface ResourceService {

    /**
     * 插入一个使用者对象
     *
     * @param model
     * @return
     */
    public int insert(Resource model);

    /**
     * 通过删除一个使用者对象
     *
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 通过id查找一个对象
     *
     * @param id
     * @return ResourceModel
     */
    public Resource get(String id);

    /**
     * 更新操作对象
     *
     * @param model
     * @return int
     */
    public int update(Resource model);


    /**
     * 查找所有的对象
     *
     * @return List < ResourceModel >
     */
    public List<Resource> findList();

    /**
     * 通过资源名获取资源
     *
     * @param resourceName
     * @return ResourceModel
     */
    public Resource getResourceByName(String resourceName);


}
