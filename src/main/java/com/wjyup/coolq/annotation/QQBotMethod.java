package com.wjyup.coolq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在CoolQ插件类的方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QQBotMethod {
    // 私聊
    boolean privateMsg() default true;

    // 群聊
    boolean groupMsg() default false;

    // 讨论组
    boolean discussMsg() default false;


}
