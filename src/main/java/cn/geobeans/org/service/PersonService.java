package cn.geobeans.org.service;


import cn.geobeans.common.model.Pager;
import cn.geobeans.dto.PersonDto;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.org.model.Org;
import cn.geobeans.org.model.Person;
import cn.geobeans.org.model.PersonOrgPos;

import java.util.List;


public interface PersonService {
    public void add(Person person);

    public void update(Person person);

    public void delete(String id);

    public Person load(String id);

    /**
     * 根据组织和岗位来获取所有的人员,组织id必须存在，岗位id如果不存在获取这个组织中的所有人员
     *
     * @param oid
     * @return
     */
    public Pager<Person> findByOrg(String oid, String posId);

    /**
     * 获取某个人员可以管理的所有的组织id
     *
     * @param pid
     * @return
     */
    public List<String> listAllOrgIdByPerson(String pid);

    /**
     * 获取这个人员的所有组织
     *
     * @param pid
     * @return
     */
    public List<Org> listAllOrgByPerson(String pid);

    /**
     * 获取某个人员的所管理的所有组织树，组织树中仅仅显示某个类型的父亲规则类型
     * 如果在添加专业功能，左边的树就显示能够添加专业的组织类型，教学部门
     *
     * @param pid
     * @param type
     * @return
     */
    public List<TreeDto> listOrgTreeByTypeParent(String pid, String type);

    /**
     * 根据用户获取组织树
     *
     * @param pid
     * @return
     */
    public List<TreeDto> listOrgTree(String pid);

    public void addPersonOrgPos(PersonOrgPos pop);

    public Pager<PersonDto> findPersonAndPosByOrg(String oid, String posId);

    public List<PersonDto> listPersonAndPosByOrg(String oid, String posId);
}
