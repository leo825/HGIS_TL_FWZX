package cn.geobeans.dto;

public class TreeDto {
    private String id;
    private String name;
    private String isParent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public TreeDto() {
    }

    public TreeDto(String id, String name, String isParent) {
        super();
        this.id = id;
        this.name = name;
        this.isParent = isParent;
    }
}