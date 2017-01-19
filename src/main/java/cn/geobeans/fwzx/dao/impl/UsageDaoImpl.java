package cn.geobeans.fwzx.dao.impl;

import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.fwzx.dao.UsageDao;
import cn.geobeans.fwzx.model.Usage;
import com.leo.util.StringUtil;

import java.util.List;

/**
 * Created by LX on 2017/1/3.
 */
public class UsageDaoImpl extends BaseDaoImpl<Usage> implements UsageDao {
    /**
     * 通过ip地址获取使用者
     *
     * @param ip 使用者ip
     * @return 返回使用者的一个实例
     */
    @Override
    public Usage getByIp(String ip) {
        String hql = "select usage from Usage usage where usage.ip=?";
        return (Usage) super.queryObject(hql, ip);
    }

    /**
     * 通过ip或者使用者名称来获取使用者
     *
     * @param ip   使用者ip
     * @param name 使用者归属
     * @return 返回使用者的列表
     */
    @Override
    public List<Usage> getListByParams(String ip, String name) {
        String hql = "select usage from Usage usage where 1=1";
        if (!StringUtil.isNull(ip)) {
            hql += " and usage.ip='" + ip + "'";
        }
        if (!StringUtil.isNull(name)) {
            hql += " and usage.name like '%" + name + "%'";
        }
        return super.list(hql);
    }
}
