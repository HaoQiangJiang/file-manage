package com.hs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于登录后才能操作的token
 * @author qiaoyn
 * @date 2019/06/14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
    boolean required() default true;
}

/*RetentionPolicy.RUNTIME:这种类型的Annotations将被JVM保留,
所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用。*/
