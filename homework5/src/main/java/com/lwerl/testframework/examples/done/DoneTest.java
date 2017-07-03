package com.lwerl.testframework.examples.done;

import com.lwerl.testframework.annotation.Test;

public class DoneTest {

    @Test
    public void test2() {
    }

    @Test(exception = RuntimeException.class)
    public void test1() {
        throw new NullPointerException();
    }
}