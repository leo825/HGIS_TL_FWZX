/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/10/9
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
package cn.geobeans.common.enums;

/**
 * Created by Administrator on 2016/10/9.
 */
public enum OperateDescriptionEnum {

    IP_NOT_ALLOW("IP不被系统允许"),
    REQUEST_UNREACHABLE("访问地址不可到达"),
    OPERATE_SUCCESS("访问正常"),
    RESPONSE_DATA_ERROR("返回数据异常"),
    PERMISSION_DENIED("没有访问此应用的权限");

    private final String text;

    OperateDescriptionEnum(String str) {
        this.text = str;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
