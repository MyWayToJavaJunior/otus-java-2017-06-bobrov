package com.lwerl.testframework.examples.before_after;

import com.lwerl.testframework.annotation.After;
import com.lwerl.testframework.annotation.Test;

public class AfterSignature {
    @After
    public void after(int a) {
    }

    @Test
    public void test() {

    }
}
