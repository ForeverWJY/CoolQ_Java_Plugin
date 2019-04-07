package com.wjyup.coolq.eventbus;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XEventArg {
	Class<? extends XEvent>[] event();
}
