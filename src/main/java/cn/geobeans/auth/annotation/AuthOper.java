package cn.geobeans.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthOper {
    /**
     * 根据方法名来获取名称，如果为""就默认按照以下规则设定
     * 1、add开头表示添加，update开头表示更新，delete开头表示删除，其余表示查询
     * 2、如果写了name就按照name的名称
     *
     * @return
     */
    public String name() default "";

    /**
     * 如果没有写sn，按照以下规则定义
     * 1、add开头-->ADD，update-->update，DELETE开头表示删除，其余表示查询
     *
     * @return
     */
    public String sn() default "";

    /**
     * 如果为-1按照以下规则
     * :0-->CREATE|ADD,1-->READ,2-->UPDATE,3-->DELETE其他
     *
     * @return
     */
    public int index() default -1;
}
