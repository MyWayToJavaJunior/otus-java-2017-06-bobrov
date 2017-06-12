package org.lwerl.objectsize.factory;

/**
 * Created by lWeRl on 12.06.2017.
 */
public class MyClassSize extends InstanceSize {
    @Override
    void run() {
        getFreeMemory();
        Object[] s = new Object[count];
        long init = getFreeMemory();
        for (int j = 0; j < count; ++j) {
            s[j] = new MyClass();
        }
        memoryDiff = init - getFreeMemory();
        instance = s[0];
    }

    static class MyClass {
        boolean b1;
        short b2;
        int b3;
        long b4;
        String s;
    }
}
