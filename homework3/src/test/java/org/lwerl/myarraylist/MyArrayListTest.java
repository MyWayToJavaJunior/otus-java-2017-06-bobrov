package org.lwerl.myarraylist;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lWeRl on 19.06.17.
 * Some test
 */
public class MyArrayListTest {
    List<Integer> actual;
    List<Integer> expected;
    List<Integer> some1 = Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    List<Integer> some2 = Arrays.asList(205, 204, 203, 202, 201);

    @Before
    public void before() {
        actual = new MyArrayList<>(some1);
        expected = new ArrayList<>(some1);
    }

    @Test
    public void constructorTest() {
        assertThat(actual, is(expected));
    }

    @Test
    public void addAllTest1() {
        Collections.addAll(actual, 1, 10, 20, 30);
        Collections.addAll(expected, 1, 10, 20, 30);
        assertThat(actual, is(expected));
    }

    @Test
    public void addAllTest2() {
        actual.clear();
        expected.clear();
        Collections.addAll(actual, 1, 10, 20, 30);
        Collections.addAll(expected, 1, 10, 20, 30);
        assertThat(actual, is(expected));
    }

    @Test
    public void copyTest1() {
        Collections.copy(actual, some2);
        Collections.copy(expected, some2);
        assertThat(actual, is(expected));
    }

    @Test
    public void sortTest1() {
        Collections.sort(actual, Integer::compare);
        Collections.sort(expected, Integer::compare);
        assertThat(actual, is(expected));
    }

    @Test
    public void sortTest2() {
        Collections.sort(actual, ((o1, o2) -> -1));
        Collections.sort(expected, ((o1, o2) -> -1));
        assertThat(actual, is(expected));
    }

}