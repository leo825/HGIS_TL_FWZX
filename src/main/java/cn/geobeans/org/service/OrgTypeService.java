package cn.geobeans.org.service;


import cn.geobeans.dto.OrgTypeDto;
import cn.geobeans.dto.OrgTypeRuleDto;
import cn.geobeans.org.model.OrgType;
import cn.geobeans.org.model.OrgTypeRule;

import java.util.List;


public interface OrgTypeService {
	public void add(OrgType orgType);
	
	public void update(OrgType orgType);
	
	public void delete(String id);
	
	public OrgType load(String id);
	
	/**
	 * 获取所有的组织类型
	 * @return
	 */
	public List<OrgType> list();
	public OrgType loadBySn(String sn);
	/**
	 * 添加组织类型规则
	 * @param pid
	 * @param cid
	 * @param num
	 */
	public void addOrgTypeRule(String pid, String cid, Integer num);

	/**
	 * 删除组织类型
	 * @param pid
	 * @param cid
	 */
	public void deleteOrgTypeRule(String pid, String cid);
	/**
	 * 更新组织类型规则，确切的说就是更新组织之间的关系的数量
	 * @param pid
	 * @param cid
	 * @param num
	 */
	public void updateOrgTypeRule(String pid, String cid, Integer num);
	/**
	 * 根据父亲id获取该组织的所有可以管理的子节点
	 * @param pid
	 * @return
	 */
	public List<OrgType> listByRule(String pid);
	/**
	 * 根据父id和子id获取两个之间的数量
	 * @param pid
	 * @param cid
	 * @return
	 */
	public int loadOrgRuleNum(String pid, String cid);
	
	/**
	 * 根据父节点获取子结点，子节点是个DTO对象，里面存储了父节点的名称、子节点的名称和数量
	 * @param pid
	 * @return
	 */
	public List<OrgTypeDto> listChildType(String pid);
	
	public List<OrgTypeRuleDto> listOrgTypeRuleByOrg(String id);

	/**
	 * 增加一个类型规则
	 * @param orgTypeRule 类型规则对象
	 */
	public void addOrgTypeRule(OrgTypeRule orgTypeRule);
	
}
