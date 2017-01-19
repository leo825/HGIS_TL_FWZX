package cn.geobeans.org.dao;


import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.org.model.Position;

import java.util.List;

public interface PositionDao extends BaseDao<Position> {
    public List<Position> find();

    public Position loadBySn(String sn);

    /**
     * 获取某个组织中存在的所有岗位列表
     *
     * @param orgId
     * @return
     */
    public List<Position> listByOrg(String orgId);
}
