package com.lwerl.testframework.examples.fail;

import com.lwerl.testframework.annotation.Test;

public class FailTest {

    @Test
    public void test4() {
        throw new AssertionError();
    }

    @Test
    public void test2() {
        throw new AssertionError("Assertions Message");
    }

    @Test(exception = NullPointerException.class)
    public void test1() {
    }

    @Test
    public void test3() {
        throw new RuntimeException();
    }

    @Test
    public void test5(int a) {
    }
}