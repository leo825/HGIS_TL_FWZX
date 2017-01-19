package cn.geobeans.org.service.impl;

import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.PersonDto;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.kit.BasicSysKit;
import cn.geobeans.org.dao.OrgDao;
import cn.geobeans.org.dao.PersonDao;
import cn.geobeans.org.model.Org;
import cn.geobeans.org.model.Person;
import cn.geobeans.org.model.PersonOrgPos;
import cn.geobeans.org.service.PersonService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service("personService")
public class PersonServiceImpl extends AbstractBaseServiceImpl implements
        PersonService {

    @Inject
    private PersonDao personDao;
    @Inject
    private OrgDao orgDao;

    @Override
    public void add(Person person) {
        personDao.add(person);
    }

    @Override
    public void update(Person person) {
        personDao.update(person);
    }

    @Override
    public void delete(String id) {
        personDao.delete(id);
    }

    @Override
    public Person load(String id) {
        return personDao.load(id);
    }

    @Override
    public Pager<Person> findByOrg(String oid, String posId) {
        return personDao.findByOrg(oid, posId);
    }

    @Override
    public void addPersonOrgPos(PersonOrgPos pop) {
        personDao.addPersonOrgPos(pop);
    }

    @Override
    public Pager<PersonDto> findPersonAndPosByOrg(String oid, String posId) {
        return personDao.findPersonAndPosByOrg(oid, posId);
    }

    @Override
    public List<String> listAllOrgIdByPerson(String pid) {
        List<Org> orgs = orgDao.listPersonOrg(pid);
        List<String> ids = new ArrayList<String>();
        for (Org org : orgs) {
            List<String> tids = orgDao.listAllChildIdsByOrg(org.getId());
            BasicSysKit.mergeList(ids, tids);
        }
        return ids;
    }

    @Override
    public List<TreeDto> listOrgTree(String pid) {
        List<Org> orgs = orgDao.listPersonOrg(pid);
        List<TreeDto> tds = new ArrayList<TreeDto>();
        for (Org org : orgs) {
            List<TreeDto> ttds = orgDao.listAllChildTreeByOrg(org.getId());
            BasicSysKit.mergeList(tds, ttds);
        }
        return tds;
    }

    @Override
    public List<Org> listAllOrgByPerson(String pid) {
        List<Org> orgs = orgDao.listPersonOrg(pid);
        List<Org> ors = new ArrayList<Org>();
        for (Org org : orgs) {
            List<Org> tors = orgDao.listAllChildByOrg(org.getId());
            BasicSysKit.mergeList(ors, tors);
        }
        return ors;
    }

    @Override
    public List<TreeDto> listOrgTreeByTypeParent(String pid, String typezz) {
        return null;
    }

    @Override
    public List<PersonDto> listPersonAndPosByOrg(String oid, String posId) {
        return personDao.listPersonAndPosByOrg(oid, posId);
    }

}
