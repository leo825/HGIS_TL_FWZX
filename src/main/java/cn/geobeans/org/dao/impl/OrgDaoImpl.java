package cn.geobeans.org.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.kit.BasicSysKit;
import cn.geobeans.org.dao.OrgDao;
import cn.geobeans.org.model.Org;
import cn.geobeans.org.model.OrgRule;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("orgDao")
public class OrgDaoImpl extends BaseDaoImpl<Org> implements OrgDao {


    @Override
    public Pager<Org> findByParent(String pid, String typeId) {
        String hql = "select org from Org org where";
        if (pid == null) {
            hql += " org.parent is null";
        } else {
            hql += " org.parent.id='" + pid + "'";
        }
        if (typeId != null) {
            hql += " and org.typeId='" + typeId + "'";
        }
        return super.find(hql);
    }

    @Override
    public List<TreeDto> tree() {
        String hql = "select new cn.geobeans.dto.TreeDto(org.id,org.name,org.parent.id) from Org org";
        return super.listObj(hql);
    }

    @Override
    public int loadNumByType(String pid, String type) {
        String hql = "select count(*) from Org where typeId=?";
        if (pid == null) {
            hql += " and parent is null";
        } else {
            hql += " and parent.id='" + pid + "'";
        }
        return ((Long) (super.queryObject(hql, type))).intValue();
    }

    @Override
    public void addRule(String orgId, String cid) {
        OrgRule oru = this.loadManagerOrg(orgId);
        String managerOrg = null;
        if (BasicSysKit.isEmpty(oru)) {
            managerOrg = "";
            managerOrg += "(" + cid + ")";
            oru = new OrgRule();
            oru.setOrgId(orgId);
            oru.setManagerOrg(managerOrg);
            super.addEntity(oru);
            return;
        }
        managerOrg = oru.getManagerOrg();
        if (BasicSysKit.isEmpty(managerOrg)) managerOrg = "";
        if (managerOrg.indexOf("(" + cid + ")") > 0)
            return;
        managerOrg += "(" + cid + ")";
        oru.setManagerOrg(managerOrg);
        super.updateEntity(oru);
    }

    @Override
    public void deleteRule(String orgId, String cid) {
        OrgRule oru = this.loadManagerOrg(orgId);
        String managerOrg = oru.getManagerOrg();
        //(22)-->((22))(22)(33)
        managerOrg = managerOrg.replace("(" + cid + ")", "");
        oru.setManagerOrg(managerOrg);
        super.updateEntity(oru);
    }

    @Override
    public List<String> listAllChildIdsByOrg(String id) {
        //首先判断这个节点类型，如果是直线型，就直接返回listChildByOrg
        Org org = super.load(id);
        List<String> orgIds = new ArrayList<String>();
        switch (org.getManagerType()) {
            case OrgRule.NO_TYPE:
                break;
            case OrgRule.DEFAULT_TYPE:
                orgIds = listChildIdsByOrg(id);
                break;
            case OrgRule.ALL_TYPE:
                orgIds = listAllOrgId();
                break;
            case OrgRule.DEF_TYPE:
                List<String> managerIds = listManagerRuleIds(id);
                List<Org> orgs = listChildOrgByIds(managerIds);
                orgIds = orgs2OrgIds(orgs);
                break;
            default:
                break;
        }
        return orgIds;
    }

    private List<String> listAllOrgId() {
        String hql = "select org.id from Org org order by org.id";
        return super.listObj(hql);
    }

    /**
     * 通过几个ids获取相应的组织机构的节点
     *
     * @param ids
     * @return
     */
    private List<Org> listChildOrgByIds(List<String> ids) {
        List<Org> orgs = listAllOrg();
        Map<String, List<Org>> corgs = org2map(orgs);
        List<Org> rorgs = new ArrayList<Org>();
        for (String id : ids) {
            rorgs.add(this.load(id));
            getOrgByMap(corgs, id, rorgs);
        }
        return rorgs;
    }

    @Override
    public List<String> listChildIdsByOrg(String id) {
        /*
         * 获取某个组织机构下面的所有组织的子节点，通常使用的是递归，使用递归会反复的查询数据库，效率不高
		 * 可以考虑使用map来替代反复查询数据库的递归操作。
		 */
        //1、取出所有的数据
        List<Org> orgs = listAllOrg();
        //2、格式化为一个map
        Map<String, List<Org>> corgs = org2map(orgs);
        //3、通过map来获取所有的子节点数据
        List<Org> rorgs = new ArrayList<Org>();
        rorgs.add(this.load(id));
        getOrgByMap(corgs, id, rorgs);
        List<String> orgIds = orgs2OrgIds(rorgs);
        return orgIds;
    }

    private List<String> orgs2OrgIds(List<Org> orgs) {
        List<String> orgIds = new ArrayList<String>();
        for (Org to : orgs) {
            orgIds.add(to.getId());
        }
        return orgIds;
    }

    private List<Org> listAllOrg() {
        String hql = "select org from Org org left join fetch org.parent order by org.id";
        List<Org> orgs = super.list(hql);
        return orgs;
    }

    private void getOrgByMap(Map<String, List<Org>> orgs, String id, List<Org> corgs) {
        if (!orgs.containsKey(id)) return;
        List<Org> torgs = orgs.get(id);
        for (Org o : torgs) {
            corgs.add(o);
            //org的子节点中如果还是orgs的key，就说明该子节点中依然是某个父节点，此时就通过递归获取数据
            if (orgs.containsKey(o.getId())) {
                getOrgByMap(orgs, o.getId(), corgs);
            }
        }
    }

    private Map<String, List<Org>> org2map(List<Org> orgs) {
        Map<String, List<Org>> maps = new HashMap<String, List<Org>>();
        List<Org> os = null;
        for (Org o : orgs) {
            if (o.getParent() == null) {
                os = new ArrayList<Org>();
                maps.put(o.getId(), os);
            } else {
                if (maps.containsKey(o.getParent().getId())) {
                    maps.get(o.getParent().getId()).add(o);
                } else {
                    os = new ArrayList<Org>();
                    if (!os.contains(o))
                        os.add(o);
                    maps.put(o.getParent().getId(), os);
                }
            }
        }
        return maps;
    }

    private OrgRule loadManagerOrg(String orgId) {
        //1,2,4,5,12,55
        String hql = "select oru from OrgRule oru where oru.orgId=?";
        //(12)(33)(22)
        return (OrgRule) super.queryObject(hql, orgId);
    }

    public List<String> listManagerRuleIds(String orgId) {
        String managerOrg = loadManagerOrg(orgId).getManagerOrg();
        List<String> ids = BasicSysKit.braceStr2List(managerOrg);
        return ids;
    }

    @Override
    public int getMaxOrder(String pid) {
        return super.getMaxOrder(pid, "Org");
    }

    @Override
    public List<Org> listAllChildByOrg(String id) {
        //首先判断这个节点类型，如果是直线型，就直接返回listChildByOrg
        Org org = super.load(id);
        List<Org> orgs = new ArrayList<Org>();
        switch (org.getManagerType()) {
            case OrgRule.NO_TYPE:
                break;
            case OrgRule.DEFAULT_TYPE:
                orgs = listChildByOrg(id);
                break;
            case OrgRule.ALL_TYPE:
                orgs = listAllOrg();
                break;
            case OrgRule.DEF_TYPE:
                List<String> managerIds = listManagerRuleIds(id);
                orgs = listChildOrgByIds(managerIds);
                break;
            default:
                break;
        }
        return orgs;
    }

    @Override
    public List<Org> listChildByOrg(String id) {
        /*
		 * 获取某个组织机构下面的所有组织的子节点，通常使用的是递归，使用递归会反复的查询数据库，效率不高
		 * 可以考虑使用map来替代反复查询数据库的递归操作。
		 */
        //1、取出所有的数据
        List<Org> orgs = listAllOrg();
        //2、格式化为一个map
        Map<String, List<Org>> corgs = org2map(orgs);
        //3、通过map来获取所有的子节点数据
        List<Org> rorgs = new ArrayList<Org>();
        rorgs.add(this.load(id));
        getOrgByMap(corgs, id, rorgs);
        return rorgs;
    }

    @Override
    public List<TreeDto> listChildTreeByOrg(String id) {
        List<Org> orgs = listChildByOrg(id);
        return orgs2Trees(orgs);
    }

    private List<TreeDto> orgs2Trees(List<Org> orgs) {
        List<TreeDto> tds = new ArrayList<TreeDto>();
        TreeDto td = null;
        for (Org org : orgs) {
            if (!BasicSysKit.isEmpty(org.getParent())) {
                td = new TreeDto(org.getId(), org.getName(), org.getParent().getId());
            } else {
                td = new TreeDto(org.getId(), org.getName(), "");
            }
            tds.add(td);
        }
        return tds;
    }

    @Override
    public List<TreeDto> listAllChildTreeByOrg(String id) {
        //首先判断这个节点类型，如果是直线型，就直接返回listChildByOrg
        Org org = super.load(id);
        List<TreeDto> tds = new ArrayList<TreeDto>();
        switch (org.getManagerType()) {
            case OrgRule.NO_TYPE:
                break;
            case OrgRule.DEFAULT_TYPE:
                tds = listChildTreeByOrg(id);
                break;
            case OrgRule.ALL_TYPE:
                tds = tree();
                break;
            case OrgRule.DEF_TYPE:
                List<String> managerIds = listManagerRuleIds(id);
                tds = listChildTreeByOrgs(managerIds);
                break;
            default:
                break;
        }
        return tds;
    }

    @Override
    public List<TreeDto> listChildTreeByOrgs(List<String> ids) {
        List<Org> orgs = this.listChildOrgByIds(ids);
        return orgs2Trees(orgs);
    }

    @Override
    public int getOrgNumsByType(String typeId) {
        String hql = "select count(*) from Org org where org.typeId=?";
        Long c = (Long) super.queryObject(hql, typeId);
        return c.intValue();
    }

    @Override
    public void addRule(String orgId, String[] cids) {
        StringBuffer sb = new StringBuffer("");
        for (String cid : cids) {
            sb.append("(").append(cid).append(")");
        }
        OrgRule oru = this.loadManagerOrg(orgId);
        if (oru == null) {
            oru = new OrgRule();
            oru.setOrgId(orgId);
            oru.setManagerOrg(sb.toString());
            super.addEntity(oru);
        } else {
            oru.setManagerOrg(sb.toString());
            super.updateEntity(oru);
        }
    }

    @Override
    public List<Org> listPersonOrg(String pid) {
        String hql = "select new Org(org.id,org.name) from Org org,PersonOrgPos pop where org.id=pop.orgId and pop.personId=?";
        return super.list(hql, pid);
    }

    @Override
    public List<TreeDto> listParentTreeByOrgType(String sn) {
        String hql = "select org from Org org,OrgType ot,OrgTypeRule otr where ot.sn=? and ot.id=otr.cid and otr.pid=org.typeId";
        List<Org> orgs = super.list(hql, sn);
        return orgs2Trees(orgs);
    }
}
