package cn.geobeans.dto;

public class PersonDto {
    private String pid;
    private String name;
    private String sfzh;
    private Integer sex;
    private String phone;
    private String posName;
    private String posId;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public PersonDto() {
    }

    public PersonDto(String pid, String name, String sfzh, Integer sex,
                     String phone, String posName, String posId) {
        super();
        this.pid = pid;
        this.name = name;
        this.sfzh = sfzh;
        this.sex = sex;
        this.phone = phone;
        this.posName = posName;
        this.posId = posId;
    }


}
