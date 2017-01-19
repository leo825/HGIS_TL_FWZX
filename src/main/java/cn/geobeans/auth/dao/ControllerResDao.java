package cn.geobeans.auth.dao;


import cn.geobeans.auth.model.ControllerOper;
import cn.geobeans.auth.model.ControllerResources;
import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.dto.TreeDto;

import java.util.List;


public interface ControllerResDao extends BaseDao<ControllerResources> {
    public void addResources(ControllerResources cr, String psn);

    public void addOper(ControllerOper oper, String rsn);

    public void updateOper(ControllerOper oper);

    public void deleteOper(String operId);

    public List<ControllerResources> listResByParent(String pid);

    /**
     * 根据资源id列表资源的操作对象
     *
     * @param rid
     * @return
     */
    public List<ControllerOper> listOperByRes(String rid);

    public ControllerOper loadOperById(String operId);

    /**
     * 通过资源的sn和操作的sn获取操作对象
     *
     * @param rsn
     * @param sn
     * @return
     */
    public ControllerOper loadOperBySn(String rsn, String sn);

    public List<TreeDto> tree();

    public ControllerResources loadBySn(String sn);
}
