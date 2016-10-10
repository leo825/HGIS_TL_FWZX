/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/10/9
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
package cn.geobeans.common.enums;

/**
 * Created by Administrator on 2016/10/9.
 */
public enum OperateResultEnum {
    SUCCESS("成功"), FAILD("失败");

    private final String text;

    OperateResultEnum(String str) {
        this.text = str;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
