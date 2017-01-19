package cn.geobeans.auth.annotation;

import cn.geobeans.auth.model.AuthFinalVal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface NavMenu {
    public String name();

    /**
     * 默认使用类名作为菜单sn
     *
     * @return
     */
    public String sn() default "";

    public int menuPos() default AuthFinalVal.MENU_LEFT_NAV;

    public String icon() default "icon-default";

    public int orderNum();

    public String psn();

    public String href() default "";

    public int display() default 1;
}
