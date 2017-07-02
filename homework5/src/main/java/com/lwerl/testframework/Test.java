package com.lwerl.testframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {

    Class<? extends Throwable> exception() default Empty.class;

    class Empty extends Throwable {
    }
}
