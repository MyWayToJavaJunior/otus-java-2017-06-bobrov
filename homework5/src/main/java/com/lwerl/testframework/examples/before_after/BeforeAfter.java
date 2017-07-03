package com.lwerl.testframework.examples.before_after;

import com.lwerl.testframework.annotation.After;
import com.lwerl.testframework.annotation.Before;
import com.lwerl.testframework.annotation.Test;

public class BeforeAfter {

    static Integer s = 0;
    Integer i = 0;


    @Before
    public void before1() {
        s++;
        i++;
    }

    @After
    public void after1() {
        s-=2;
        i-=2;
    }

    @Test
    public void test1() {
        if ( i != 2 && s!= 2) throw new AssertionError();
    }

    @After
    public void after2() {
        s-=2;
        i-=2;
    }

    @Before
    public void before2() {
        s++;
        i++;
    }

    @Test
    public void test2() {
        if ( i != 2 && s!= 0) throw new AssertionError();
    }
}
