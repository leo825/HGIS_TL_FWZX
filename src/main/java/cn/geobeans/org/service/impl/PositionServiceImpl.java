package cn.geobeans.org.service.impl;

import cn.geobeans.common.model.SysException;
import cn.geobeans.org.dao.PositionDao;
import cn.geobeans.org.model.Position;
import cn.geobeans.org.service.PositionService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("positionService")
public class PositionServiceImpl extends AbstractBaseServiceImpl implements PositionService {
    @Inject
    private PositionDao positionDao;

    @Override
    public void add(Position pos) {
        if (positionDao.loadBySn(pos.getSn()) != null) throw new SysException("添加的岗位的sn已经存在");
        positionDao.add(pos);
    }

    @Override
    public void update(Position pos) {
        //if(positionDao.loadBySn(pos.getSn())!=null) throw new SysException("添加的岗位的sn已经存在");
        positionDao.update(pos);
    }

    @Override
    public void delete(String id) {
        positionDao.delete(id);
    }

    @Override
    public Position load(String id) {
        return positionDao.load(id);
    }

    @Override
    public List<Position> find() {
        return positionDao.find();
    }

    @Override
    public List<Position> listByOrg(String orgId) {
        return positionDao.listByOrg(orgId);
    }

}
