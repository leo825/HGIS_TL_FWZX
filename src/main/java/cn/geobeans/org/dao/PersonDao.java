package cn.geobeans.org.dao;


import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.PersonDto;
import cn.geobeans.org.model.Person;
import cn.geobeans.org.model.PersonOrgPos;

import java.util.List;


public interface PersonDao extends BaseDao<Person> {
    /**
     * 根据组织和岗位来获取所有的人员,组织id必须存在，岗位id如果不存在获取这个组织中的所有人员
     *
     * @param oid 组织机构id
     * @return
     */
    public Pager<Person> findByOrg(String oid, String posId);

    public Pager<PersonDto> findPersonAndPosByOrg(String oid, String posId);

    public List<PersonDto> listPersonAndPosByOrg(String oid, String posId);

    public void addPersonOrgPos(PersonOrgPos pop);
}
