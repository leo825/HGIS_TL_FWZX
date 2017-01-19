package cn.geobeans.org.dao;


import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.org.model.Org;

import java.util.List;


public interface OrgDao extends BaseDao<Org> {

    /**
     * 根据父id和组织类型查到相应的组织机构
     *
     * @param pid    组织父id
     * @param typeId 组织类型
     * @return 返回组织机构的分页
     */
    public Pager<Org> findByParent(String pid, String typeId);

    /**
     * 根据组织类型来生成这颗树，如果tid为null，就获取所有的组织
     *
     * @return 返回组织树
     */
    public List<TreeDto> tree();

    /**
     * 根据组织类型，获取该父节点下究竟有多少个子组织，在添加组织的时候可以确定是否可以添加(添加的时候判断)
     *
     * @param pid    组织机构父节点
     * @param typeId 节点类型
     * @return
     */
    public int loadNumByType(String pid, String typeId);

    /**
     * 添加某个组织机构可以管理的子组织的id，这个子组织的id其实就是可以管理的所有子节点id
     *
     * @param orgId 组织机构id
     * @param cid   子组织id
     */
    public void addRule(String orgId, String cid);

    /**
     * 添加一组子组织机构
     *
     * @param orgId 组织机构id
     * @param cids  子组织id的数组
     */
    public void addRule(String orgId, String[] cids);

    /**
     * 删除子组织
     *
     * @param orgId 组织机构id
     * @param cid   子组织id
     */
    public void deleteRule(String orgId, String cid);

    /**
     * 获取某个组织下面的所有子组织id
     * 需要进行判断，如果组织类型是NO_TYPE-->return null
     * 如果组织类型是DEFAULT_TYPE-->返回该组织的所有子节点
     * 如果组织类型是ALL_TYPE-->返回所有组织
     * 如果组织类型是DEF_TYPE-->获取所有子组织下的子节点
     *
     * @param id 组织机构id
     * @return 返回所有的子组织id
     */
    public List<String> listAllChildIdsByOrg(String id);

    /**
     * 获取某个组织下面的所有直线管理的子组织
     *
     * @param id 组织机构id
     * @return 返回
     */
    public List<String> listChildIdsByOrg(String id);

    /**
     * 获取某个组织机构下面的所有子组织节点
     *
     * @param id 组织机构id
     * @return 返回所有的组织机构列表
     */
    public List<Org> listAllChildByOrg(String id);

    /**
     * 获取某个组织机构下面的组织机构
     *
     * @param id 组织结构id
     * @return 返回组织机构列表
     */
    public List<Org> listChildByOrg(String id);

    /**
     * 获取某个子组织下的节点树
     *
     * @param id 组织机构id
     * @return 返回某个组织下的节点树
     */
    public List<TreeDto> listChildTreeByOrg(String id);

    /**
     * 获取某个子组织下的所有的组织，也需要根据类型来进行获取
     *
     * @param id 组织机构id
     * @return
     */
    public List<TreeDto> listAllChildTreeByOrg(String id);

    /**
     * 根据一组ids获取树对象
     *
     * @param ids 一组组织机构id
     * @return
     */
    public List<TreeDto> listChildTreeByOrgs(List<String> ids);

    /**
     * 根据一个组织机构类型的编号获取父类的所有的组织机构节点，并且生成一棵树
     *
     * @param sn 组织机构类型的编号
     * @return
     */
    public List<TreeDto> listParentTreeByOrgType(String sn);

    /**
     * 获取最大的Order值
     *
     * @param pid 组织机构id
     * @return
     */
    public int getMaxOrder(String pid);

    /**
     * 根据组织机构类型id获取当前的组织数
     *
     * @param typeId 组织机构类型
     * @return
     */
    public int getOrgNumsByType(String typeId);

    /**
     * 获取某个组织下的所有规则id
     *
     * @param orgId 组织机构id
     * @return
     */
    public List<String> listManagerRuleIds(String orgId);

    /**
     * 获取某个人所在的所有组织
     *
     * @param pid 人员id
     * @return 返回组织机构列表
     */
    public List<Org> listPersonOrg(String pid);

}
