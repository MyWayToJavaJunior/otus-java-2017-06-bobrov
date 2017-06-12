package org.lwerl.objectsize.factory;

/**
 * Created by lWeRl on 12.06.2017.
 */
public class ArrayZeroLengthSize extends InstanceSize {
    @Override
    void run() {
        getFreeMemory();
        Object[] s = new Object[count];
        long init = getFreeMemory();
        for (int j = 0; j < count; ++j) {
            s[j] = new int[0];
        }
        memoryDiff = init - getFreeMemory();
        instance = s[0];
    }
}
