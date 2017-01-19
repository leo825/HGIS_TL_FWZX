package cn.geobeans.org.service.impl;

import cn.geobeans.common.model.SysException;
import cn.geobeans.dto.OrgTypeDto;
import cn.geobeans.dto.OrgTypeRuleDto;
import cn.geobeans.org.dao.OrgDao;
import cn.geobeans.org.dao.OrgTypeDao;
import cn.geobeans.org.model.OrgType;
import cn.geobeans.org.model.OrgTypeRule;
import cn.geobeans.org.service.OrgTypeService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("orgTypeService")
public class OrgTypeServiceImpl extends AbstractBaseServiceImpl implements OrgTypeService {
    @Inject
    private OrgTypeDao orgTypeDao;
    @Inject
    private OrgDao orgDao;

    @Override
    public void add(OrgType orgType) {
        if (orgTypeDao.loadBySn(orgType.getSn()) != null)
            throw new SysException("要添加的组织机构类型的sn已经存在");
        orgTypeDao.add(orgType);
    }

    @Override
    public void update(OrgType orgType) {
        orgTypeDao.update(orgType);
    }

    @Override
    public void delete(String id) {
        int c = orgDao.getOrgNumsByType(id);
        if (c > 0) throw new SysException("要删除的组织机构类型中有组织存在，不能删除");
        System.out.println(c);
        orgTypeDao.delete(id);
    }

    @Override
    public OrgType load(String id) {
        return orgTypeDao.load(id);
    }

    @Override
    public List<OrgType> list() {
        return orgTypeDao.list();
    }

    @Override
    public OrgType loadBySn(String sn) {
        return orgTypeDao.loadBySn(sn);
    }

    @Override
    public void addOrgTypeRule(String pid, String cid, Integer num) {
        orgTypeDao.addOrgTypeRule(pid, cid, num);
    }

    @Override
    public void deleteOrgTypeRule(String pid, String cid) {
        orgTypeDao.deleteOrgTypeRule(pid, cid);
    }

    @Override
    public void updateOrgTypeRule(String pid, String cid, Integer num) {
        orgTypeDao.updateOrgTypeRule(pid, cid, num);
    }

    @Override
    public List<OrgType> listByRule(String pid) {
        return orgTypeDao.listByRule(pid);
    }

    @Override
    public int loadOrgRuleNum(String pid, String cid) {
        return orgTypeDao.loadOrgRuleNum(pid, cid);
    }

    @Override
    public List<OrgTypeDto> listChildType(String pid) {
        return orgTypeDao.listChildType(pid);
    }

    @Override
    public List<OrgTypeRuleDto> listOrgTypeRuleByOrg(String id) {
        return orgTypeDao.listOrgTypeRuleByOrg(id);
    }

    /**
     * 增加一个类型规则
     *
     * @param orgTypeRule 类型规则对象
     */
    @Override
    public void addOrgTypeRule(OrgTypeRule orgTypeRule) {
        orgTypeDao.addOrgTypeRule(orgTypeRule);
    }

}
