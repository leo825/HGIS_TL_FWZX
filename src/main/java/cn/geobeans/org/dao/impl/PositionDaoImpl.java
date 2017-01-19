package cn.geobeans.org.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.org.dao.PositionDao;
import cn.geobeans.org.model.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("positionDao")
public class PositionDaoImpl extends BaseDaoImpl<Position> implements PositionDao {

    @Override
    public List<Position> find() {
        return super.list("from Position");
    }

    @Override
    public Position loadBySn(String sn) {
        return (Position) super.loadBySn(sn, Position.class.getName());
    }

    @Override
    public List<Position> listByOrg(String orgId) {
        String hql = "select distinct p from Position p,PersonOrgPos pop where pop.posId=p.id and pop.orgId=?";
        return super.list(hql, orgId);
    }

}
