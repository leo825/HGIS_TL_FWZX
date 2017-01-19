package cn.geobeans.fwzx.dao;

import cn.geobeans.common.dao.BaseDao;
import cn.geobeans.fwzx.model.Usage;

import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
public interface UsageDao extends BaseDao<Usage> {

    /**
     * 通过ip地址获取使用者
     *
     * @param ip 使用者ip
     * @return 返回使用者的一个实例
     */
    public Object getByIp(String ip);


    /**
     * 通过ip或者使用者名称来获取使用者
     *
     * @param ip   使用者ip
     * @param name 使用者归属
     * @return 返回使用者的列表
     */
    public List<Usage> getListByParams(String ip, String name);
}
