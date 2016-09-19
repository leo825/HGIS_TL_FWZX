package cn.geobeans.common.enums;

/**
 * Created by ghx on 2015/1/9.
 */
public enum JsonResponseStatusEnum {

    SUCCESS("success"),ERROR("error");

    private final String text;

    JsonResponseStatusEnum(String str) {
        this.text = str;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
