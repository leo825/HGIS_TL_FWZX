package cn.geobeans.org.dao;


import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.dto.OrgTypeDto;
import cn.geobeans.dto.OrgTypeRuleDto;
import cn.geobeans.org.model.OrgType;
import cn.geobeans.org.model.OrgTypeRule;

import java.util.List;


public interface OrgTypeDao extends BaseDao<OrgType> {
    /**
     * 获取所有的组织类型
     *
     * @return 返回组织类型的列表
     */
    public List<OrgType> list();

    /**
     * 根据组织机构编码获取组织结构类型
     *
     * @param sn 组织机构编码
     * @return
     */
    public OrgType loadBySn(String sn);

    /**
     * 添加组织类型规则
     *
     * @param pid 组织机构父节点
     * @param cid 组织机构子节点
     * @param num 组织机构能够允许的子组织数量
     */
    public void addOrgTypeRule(String pid, String cid, Integer num);

    /**I
     * 删除组织类型
     *
     * @param pid 父组织机构节点
     * @param cid 子组织机构节点
     */
    public void deleteOrgTypeRule(String pid, String cid);

    /**
     * 更新组织类型规则，确切的说就是更新组织之间的关系的数量
     *
     * @param pid 父组织机构节点
     * @param cid 子组织机构节点
     * @param num 能够容许的组织机构数量
     */
    public void updateOrgTypeRule(String pid, String cid, Integer num);

    /**
     * 根据父亲id获取该组织的所有可以管理的子节点
     *
     * @param pid 组织机构id
     * @return 返回组织机构类型列表
     */
    public List<OrgType> listByRule(String pid);

    /**
     * 根据父节点获取子结点，子节点是个DTO对象，里面存储了父节点的名称、子节点的名称和数量
     *
     * @param pid 组织机构父节点
     * @return 返回OrgTypeDto数据
     */
    public List<OrgTypeDto> listChildType(String pid);

    /**
     * 根据组织或者该组织类型的所有的可以添加为子类型的节点
     *
     * @param id 组织机构id
     * @return 返回OrgTypeRuleDto数据
     */
    public List<OrgTypeRuleDto> listOrgTypeRuleByOrg(String id);

    /**
     * 根据父id和子id获取两个之间的数量
     *
     * @param pid 父节点
     * @param cid 子节点
     * @return 返回ruleNum
     */
    public int loadOrgRuleNum(String pid, String cid);


    /**
     * 增加一个组织类型规则
     * @param orgTypeRule 组织类型规则对象
     */
    public void addOrgTypeRule(OrgTypeRule orgTypeRule);

}
