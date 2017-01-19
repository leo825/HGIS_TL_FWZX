package cn.geobeans.dto;

/**
 * 组织机构类型的dto对象，通过该对象获取
 *
 * @author Konghao
 */
public class OrgTypeDto {
    private String cid;
    private String cname;
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

    public int getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public OrgTypeDto(String cid, String cname, Integer num) {
        super();
        this.cid = cid;
        this.cname = cname;
        this.num = num;
    }

    public OrgTypeDto() {
    }

    @Override
    public String toString() {
        return "OrgTypeDto [cid=" + cid + ", cname=" + cname + ", num=" + num
                + "]";
    }


}
