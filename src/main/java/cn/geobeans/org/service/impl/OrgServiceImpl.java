package cn.geobeans.org.service.impl;

import cn.geobeans.common.model.Pager;
import cn.geobeans.common.model.SysException;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.org.dao.OrgDao;
import cn.geobeans.org.dao.OrgTypeDao;
import cn.geobeans.org.model.Org;
import cn.geobeans.org.service.OrgService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("orgService")
public class OrgServiceImpl extends AbstractBaseServiceImpl implements OrgService {
    @Inject
    private OrgDao orgDao;

    @Inject
    private OrgTypeDao orgTypeDao;

    private void checkChildOrgNum(Org cOrg, Org pOrg) {
        if (pOrg == null) return;
        int rnum = orgTypeDao.loadOrgRuleNum(pOrg.getTypeId(), cOrg.getTypeId());
        if (rnum < 0) return;
        int hnum = orgDao.loadNumByType(pOrg.getId(), cOrg.getTypeId());
        if (hnum >= rnum) throw new SysException(pOrg.getName() + "下的" + cOrg.getName() + "的数量已经达到最大化");
    }

    //parent已经存在的添加
    @Override
    public void add(Org org) {
        checkChildOrgNum(org, org.getParent());
        if (org.getParent() == null) {
            org.setOrderNum(orgDao.getMaxOrder(null) + 1);
        } else {
            org.setOrderNum(orgDao.getMaxOrder(org.getParent().getId()) + 1);
        }
        orgDao.add(org);
    }

    @Override
    public void add(Org org, String pid) {
        if (pid != null) {
            Org p = orgDao.load(pid);
            if (p == null) throw new SysException("要添加的父亲组织不存在!");
            checkChildOrgNum(org, p);
            org.setParent(p);
        }
        org.setOrderNum(orgDao.getMaxOrder(pid) + 1);
        orgDao.add(org);
    }

    @Override
    public void update(Org org) {
        orgDao.update(org);
    }

    @Override
    public void delete(String id) {
        orgDao.delete(id);
    }

    @Override
    public Org load(String id) {
        return orgDao.load(id);
    }

    @Override
    public Pager<Org> findByParent(String pid, String typeId) {
        return orgDao.findByParent(pid, typeId);
    }

    @Override
    public List<TreeDto> tree() {
        return orgDao.tree();
    }


    @Override
    public void addRule(String orgId, String cid) {
        orgDao.addRule(orgId, cid);
    }

    @Override
    public void deleteRule(String orgId, String cid) {
        orgDao.deleteRule(orgId, cid);
    }

    @Override
    public List<String> listAllChildIdsByOrg(String id) {
        return orgDao.listAllChildIdsByOrg(id);
    }


    @Override
    public List<Org> listAllChildByOrg(String id) {
        return orgDao.listAllChildByOrg(id);
    }

    @Override
    public List<TreeDto> listAllChildTreeByOrg(String id) {
        return orgDao.listAllChildTreeByOrg(id);
    }

    @Override
    public void addRule(String orgId, String[] cids) {
        orgDao.addRule(orgId, cids);
    }

    @Override
    public List<String> listManagerRuleIds(String orgId) {
        return orgDao.listManagerRuleIds(orgId);
    }

    @Override
    public List<TreeDto> listParentTreeByOrgType(String sn) {
        return orgDao.listParentTreeByOrgType(sn);
    }

    /**
     * 增加初始化的组织机构
     *
     * @param org
     */
    @Override
    public void addInitOrg(Org org) {
        orgDao.add(org);
    }

}
