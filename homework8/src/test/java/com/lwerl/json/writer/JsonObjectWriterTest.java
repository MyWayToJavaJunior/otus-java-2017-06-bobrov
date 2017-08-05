package com.lwerl.json.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by lWeRl on 05.08.2017.
 */
public class JsonObjectWriterTest {

    private Gson gson;
    private JsonObjectWriter writer;

    @Before
    public void before() {
        gson = new GsonBuilder().setDateFormat("YYYY-MM-DD hh:mm:ss").create();
        gson.serializeNulls();
        writer = new JsonObjectWriter();
        writer.setDatePattern("YYYY-MM-DD hh:mm:ss");
    }

    @Test
    public void toJSONSimple() throws Exception {
        Object o1 = null;
        Assert.assertEquals(gson.toJson(o1), writer.toJson(o1));

        CharSequence o2 = "test";
        Assert.assertEquals(gson.toJson(o2), writer.toJson(o2));

        int o3 = 10;
        Assert.assertEquals(gson.toJson(o3), writer.toJson(o3));

        Number o4 = 10.12;
        Assert.assertEquals(gson.toJson(o4), writer.toJson(o4));

        boolean o5 = true;
        Assert.assertEquals(gson.toJson(o5), writer.toJson(o5));

        Date o6 = new Date();
        Assert.assertEquals(gson.toJson(o6), writer.toJson(o6));

        double o7 = 1.12e10;
        Assert.assertEquals(gson.toJson(o7), writer.toJson(o7));
    }

    @Test
    public void toJSONArray() throws Exception {
        int o1[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Assert.assertEquals(gson.toJson(o1), writer.toJson(o1));

        Object[] o2 = {"test", new Date(), true, null, 1000, 0.123, 1.23e-3};
        Assert.assertEquals(gson.toJson(o2), writer.toJson(o2));

        Set o3 = new HashSet(Arrays.asList(o2));
        Assert.assertEquals(gson.toJson(o3), writer.toJson(o3));

        List o4 = Arrays.asList(o2);
        Assert.assertEquals(gson.toJson(o4), writer.toJson(o4));
    }

    @Test
    public void toJSONObject() throws Exception {
        Object o1 = new Object();
        Assert.assertEquals(gson.toJson(o1), writer.toJson(o1));

        TestObject o2 = new TestObject("1", 20, false, new Date(), null);
        Assert.assertEquals(gson.toJson(o2), writer.toJson(o2));

        Map<Object, Object> o3 = new HashMap<>();
        o3.put(1, new Object());
        o3.put("test", new Date());
        o3.put(new TestObject(null, null, null, null, null), o2);
        Assert.assertEquals(gson.toJson(o3), writer.toJson(o3));

        Map<String, TestObject> o4 = new HashMap<>();
        o4.put("test1", new TestObject("1", 20, false, new Date(), new Object()));
        o4.put("test2", new TestObject(2, "28", new Date(), new Object(), true));
        o4.put("test3", new TestObject(null, 28.3, new Date(), "what!", true));
        Assert.assertEquals(gson.toJson(o4), writer.toJson(o4));
    }

    @Test
    public void toJSONComplex() throws Exception {
        int[] f1 = {1, 2, 3, 4, 5, 6, 123};
        Object[] f2 = {"test", new Date(), true, null, 1000, 0.123, 1.23e-3, new TestObject("1", 20, false, new Date(), new Object())};
        Set f3 = new HashSet(Arrays.asList(f2));
        List f4 = Arrays.asList(f2);
        Map<Object, Object> f5 = new HashMap<>();
        f5.put(1, new Object());
        f5.put("test", new Date());
        f5.put(new TestObject(null, null, null, null, null), new Object());
        TestObject o1 = new TestObject(f1, f2, f3, f4, f5);
        Assert.assertEquals(gson.toJson(o1), writer.toJson(o1));
    }

    public static class TestObject {
        private Object field1;
        private Object field2;
        private Object field3;
        private Object field4;
        private Object field5;

        public TestObject(Object field1, Object field2, Object field3, Object field4, Object field5) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
            this.field5 = field5;
        }
    }
}