package com.lwerl.examples.fail;

import com.lwerl.testframework.Test;
import org.junit.Assert;

public class FailTest {

    @Test(exception = NullPointerException.class)
    public void test1() {

    }

    @Test
    public void test2() {
        throw new AssertionError("AssertionError");
    }

    @Test
    public void test3() {
        throw new RuntimeException();
    }

    @Test
    public void test4() {
        Assert.assertTrue(1 == 2);
    }
}