package cn.geobeans.common.model;

import java.util.List;

/**
 * easyui分页查询返回结果对象
 * Created by ice on 2016/3/17.
 */
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 记录
     */
    private List<T> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
