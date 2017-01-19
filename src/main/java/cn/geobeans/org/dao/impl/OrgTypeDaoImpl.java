package cn.geobeans.org.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.dto.OrgTypeDto;
import cn.geobeans.dto.OrgTypeRuleDto;
import cn.geobeans.org.dao.OrgTypeDao;
import cn.geobeans.org.model.OrgType;
import cn.geobeans.org.model.OrgTypeRule;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("orgTypeDao")
public class OrgTypeDaoImpl extends BaseDaoImpl<OrgType> implements OrgTypeDao {


    @Override
    public List<OrgType> list() {
        String hql = "select ot from OrgType ot";
        return super.list(hql);
    }

    @Override
    public void addOrgTypeRule(String pid, String cid, Integer num) {
        OrgTypeRule otr = new OrgTypeRule();
        otr.setPid(pid);
        otr.setCid(cid);
        otr.setNum(num);
        super.addEntity(otr);
    }

    @Override
    public void deleteOrgTypeRule(String pid, String cid) {
        String hql = "delete OrgTypeRule otr where otr.cid=? and otr.pid=?";
        super.updateByHql(hql, cid, pid);
    }

    @Override
    public void updateOrgTypeRule(String pid, String cid, Integer num) {
        String hql = "update OrgTypeRule otr set otr.num=? where otr.cid=? and otr.pid=?";
        super.updateByHql(hql, num, cid, pid);
    }

    @Override
    public List<OrgType> listByRule(String pid) {

        String hql = "select ot from OrgType ot,OrgTypeRule otr where otr.cid=ot.id and otr.pid=?";
//        String hql = "select otr from OrgTypeRule otr where otr.parent.id=?";
        return super.listObj(hql, pid);
    }

    @Override
    public int loadOrgRuleNum(String pid, String cid) {
        String hql = "select ort.num from OrgTypeRule ort where ort.cid=? and ort.pid=?";
        return (Integer) super.queryObject(hql, cid, pid);
    }

    /**
     * 增加一个组织类型规则
     *
     * @param orgTypeRule 组织类型规则对象
     */
    @Override
    public void addOrgTypeRule(OrgTypeRule orgTypeRule) {
        super.addEntity(orgTypeRule);
    }

    @Override
    public OrgType loadBySn(String sn) {
        return (OrgType) super.loadBySn(sn, OrgType.class.getName());
    }


    @Override
    public List<OrgTypeDto> listChildType(String pid) {
        String hql = "select new cn.geobeans.dto.OrgTypeDto(ot2.id,ot2.name,otr.num) from " +
                "OrgType ot,OrgTypeRule otr,OrgType ot2 " +
                "where ot.id=? and ot.id=otr.pid and ot2.id=otr.cid";
        return super.listObj(hql, pid);
    }

    @Override
    public List<OrgTypeRuleDto> listOrgTypeRuleByOrg(String id) {
        List<OrgTypeDto> otds = this.listChildType(id);
        List<OrgTypeRuleDto> ortds = new ArrayList<OrgTypeRuleDto>();
        List<String> aids = new ArrayList<String>();
        OrgTypeRuleDto otrd;
        //把存在的添加进行
        for (OrgTypeDto otd : otds) {
            otrd = new OrgTypeRuleDto();
            otrd.setCid(otd.getCid());
            otrd.setCname(otd.getCname());
            otrd.setExists(true);
            otrd.setNum(otd.getNum());
            ortds.add(otrd);
            aids.add(otd.getCid());
        }
        List<OrgType> ots = this.list();
        for (OrgType ot : ots) {
            if (ot.getId() == id) continue;
            if (aids.contains(ot.getId())) continue;
            otrd = new OrgTypeRuleDto();
            otrd.setCid(ot.getId());
            otrd.setCname(ot.getName());
            otrd.setExists(false);
            otrd.setNum(0);
            ortds.add(otrd);
        }
        return ortds;
    }

}
