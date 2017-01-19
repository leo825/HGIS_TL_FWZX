package cn.geobeans.auth.service;


import cn.geobeans.auth.model.Acl;

import java.util.List;
import java.util.Map;


public interface AclService {

    public void add(Acl acl);

    /**
     * 根据主体id和主体类型，资源id和资源类型获取ACL对象
     *
     * @param pid
     * @param ptype
     * @param rid
     * @param rtype
     * @return
     */
    public Acl loadAcl(String pid, String ptype, String rid, String rtype);

    /**
     * 根据角色id、资源类型和资源id获取ACL对象
     *
     * @param roleId
     * @param rid
     * @param rtype
     * @return
     */
    public Acl loadAclByRole(String roleId, String rid, String rtype);

    /**
     * 根据用户id、资源类型和资源id获取ACL对象
     *
     * @param userId
     * @param rid
     * @param rtype
     * @return
     */
    public Acl loadAclByUser(String userId, String rid, String rtype);

    /**
     * 根据角色id、资源类型和资源id获取该角色可以访问的所有的操作的id
     *
     * @param rid
     * @param rtype
     * @param roleId
     * @return
     */
    public List<String> listRoleOperIdsByRes(String rid, String rtype, String roleId);

    /**
     * 根据用户id、资源类型和资源id获取该用户可以访问的所有的操作的id
     * 流程：1、获取该用户的所有角色，再通过角色获取可以管理的操作id
     * 2、获取该用户自己可以访问的操作id
     *
     * @param rid
     * @param rtype
     * @param userId
     * @return
     */
    public List<String> listUserOperIdsByRes(String rid, String rtype, String userId);

    /**
     * 获取用户自己说授权的所有操作id
     *
     * @param rid
     * @param rtype
     * @param userId
     * @return
     */
    public List<String> listUserSelfOperIdsByRes(String rid, String rtype, String userId);

    /**
     * 获取某个角色可以访问的所有的资源操作对象
     * 返回值是Map。key表示资源类名，value是所有可以访问的操作
     *
     * @param roleId
     * @return
     */
    public Map<String, List<String>> listAllControllerResAndOperByRole(String roleId);

    /**
     * 获取某个用户可以访问的所有的资源操作对象
     * 操作流程：1、首先获取用户所有角色对象的资源操作，2、获取自己的资源操作
     * 返回值是Map。key表示资源类名，value是所有可以访问的操作
     *
     * @param userId
     * @return
     */
    public Map<String, List<String>> listAllControllerResAndOperByUser(String userId);

    /**
     * 根据角色的id获取该角色可以访问的所有的菜单的sn
     *
     * @param roleId
     * @return
     */
    public List<String> listMenuSnByRole(String roleId);

    /**
     * 根据用户id获取该用户可以访问的所有菜单的sn
     * 流程也是先获取该用户角色的数据之后获取用户自己所授权的数据
     *
     * @param userId
     * @return
     */
    public List<String> listMenuSnByUser(String userId);

    /**
     * 根据角色的id获取该角色所有的菜单的id
     *
     * @param roleId
     * @return
     */
    public List<String> listMenuIdByRole(String roleId);

    /**
     * 根据用户的id获取该用户所有的菜单的id，先获取角色的数据，再获取用户独立的数据
     *
     * @param userId
     * @return
     */
    public List<String> listMenuIdByUser(String userId);

    /**
     * 根据用户的id获取该用户自己的菜单的id，不获取角色的数据
     *
     * @param userId
     * @return
     */
    public List<String> listMenuIdByUserSelf(String userId);
}
