package cn.geobeans.org.service;


import cn.geobeans.org.model.Position;

import java.util.List;


public interface PositionService {
    public void add(Position pos);

    public void update(Position pos);

    public void delete(String id);

    public Position load(String id);

    public List<Position> find();

    public List<Position> listByOrg(String orgId);
}
