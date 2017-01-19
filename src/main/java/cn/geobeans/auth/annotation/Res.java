package cn.geobeans.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明在controller上面，用于初始化资源对象
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Res {
    /**
     * 资源的中文名称
     *
     * @return
     */
    public String name();

    /**
     * 资源的sn
     *
     * @return
     */
    public String sn();

    /**
     * 资源的父资源的sn
     *
     * @return
     */
    public String psn();

    /**
     * 资源的排序号
     *
     * @return
     */
    public int orderNum();
}
