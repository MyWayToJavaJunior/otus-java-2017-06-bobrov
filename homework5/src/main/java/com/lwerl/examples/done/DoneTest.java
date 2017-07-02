package com.lwerl.examples.done;

import com.lwerl.testframework.Test;
import org.junit.Assert;

public class DoneTest {

    @Test(exception = RuntimeException.class)
    public void test1() {
        throw new NullPointerException();
    }

    @Test
    public void test2() {
    }

    @Test
    public void test3() {
        Assert.assertTrue(1 == 1);
    }

    @Test
    public void test4(int a) {
    }
}