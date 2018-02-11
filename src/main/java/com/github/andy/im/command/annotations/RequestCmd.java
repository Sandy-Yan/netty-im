package com.github.andy.im.command.annotations;

import java.lang.annotation.*;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 14:40
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestCmd {
    
    String[] value() default {};
}
