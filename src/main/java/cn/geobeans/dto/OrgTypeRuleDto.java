package cn.geobeans.dto;

/**
 * 组织机构类型的规则dto
 *
 * @author Konghao
 */
public class OrgTypeRuleDto {
    private String cid;
    private String cname;
    private boolean exists;
    private Integer num;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
