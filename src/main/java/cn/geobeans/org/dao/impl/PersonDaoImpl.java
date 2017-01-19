package cn.geobeans.org.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.PersonDto;
import cn.geobeans.org.dao.PersonDao;
import cn.geobeans.org.model.Person;
import cn.geobeans.org.model.PersonOrgPos;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("personDao")
public class PersonDaoImpl extends BaseDaoImpl<Person> implements PersonDao {

    @Override
    public Pager<Person> findByOrg(String oid, String posId) {
        String hql = "select p from Person p,PersonOrgPos pop where p.id=pop.personId and pop.orgId=? ";
        if (posId != null && !"".equals(posId)) {
            hql += " and pop.posId=" + posId;
        }
        return super.find(hql, oid);
    }


    @Override
    public void addPersonOrgPos(PersonOrgPos pop) {
        super.addEntity(pop);
    }

    @Override
    public Pager<PersonDto> findPersonAndPosByOrg(String oid, String posId) {
        String hql = "select new cn.geobeans.dto.PersonDto(p.id,p.name,p.sfzh,p.sex,p.phone,pos.name,pos.id) " +
                "from Person p,PersonOrgPos pop,Position pos where p.id=pop.personId and pos.id=pop.posId and pop.orgId=? ";
        if (posId != null && !"".equals(posId)) {
            hql += " and pop.posId=" + posId;
        }
        return (Pager<PersonDto>) (Object)super.find(hql, oid);
    }


    @Override
    public List<PersonDto> listPersonAndPosByOrg(String oid, String posId) {
        String hql = "select new cn.geobeans.dto.PersonDto(p.id,p.name,p.sfzh,p.sex,p.phone,pos.name,pos.id)" +
                " from Person p,PersonOrgPos pop,Position pos where p.id=pop.personId and pos.id=pop.posId and pop.orgId=?";
        if (posId != null && !"".equals(posId)) {
            hql += " and pop.posId=" + posId;
        }
        return super.listObj(hql, oid);
    }

}
