package com.lwerl.testframework.examples.before_after;

import com.lwerl.testframework.annotation.Before;
import com.lwerl.testframework.annotation.Test;

public class BeforeException {
    @Before
    public void after() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }
}
