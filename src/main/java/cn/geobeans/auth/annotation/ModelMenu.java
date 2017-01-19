package cn.geobeans.auth.annotation;


import cn.geobeans.auth.model.AuthFinalVal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelMenu {
    /**
     * 默认值:add开头表示添加，update开头更新，list开头表示列表，delete开头表示删除，show开头为查询，其余手动指定
     *
     * @return
     */
    public String name() default "";

    /**
     * 默认使用类名.方法名作为菜单sn
     *
     * @return
     */
    public String sn() default "";

    public int menuPos() default AuthFinalVal.MENU_MODEL_NAV;

    public String icon() default "icon-default";

    public int orderNum() default 0;

    /**
     * 默认为父类的psn
     *
     * @return
     */
    public String psn() default "";

    /**
     * 可以根据RequestMapping获取
     *
     * @return
     */
    public String href() default "";

    public int display() default 1;
}
