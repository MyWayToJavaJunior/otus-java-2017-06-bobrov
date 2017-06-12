package org.lwerl.objectsize.factory;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.lwerl.sizeof.Size;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * Created by lWeRl on 11.06.2017.
 */
public abstract class InstanceSize {
    final int count = 100_000;
    long memoryDiff;
    Object instance;

    static {
        System.out.println(VM.current().details());
    }

    static String memoryFormat(long bytes) {
        int byteAmount = (int) (bytes % 1024);
        int kbAmount = (int) ((bytes / 1024) % 1024);
        int mbAmount = (int) ((bytes / (1024 * 1024)) % 1024);
        int gbAmount = (int) ((bytes / (1073741824)));
        String result = "";
        result = gbAmount == 0 ? result : result + gbAmount + " GB ";
        result = mbAmount == 0 ? result : result + mbAmount + " MB ";
        result = kbAmount == 0 ? result : result + kbAmount + " KB ";
        result = byteAmount == 0 ? result : result + byteAmount + " Byte";
        return result;
    }

    static long getFreeMemory() {
        long init = Runtime.getRuntime().freeMemory(), init2;
        int count = 0;
        do {
            System.gc();
            try {
                Thread.sleep(100);
            } catch (Exception x) {
                System.out.println("Oooops");
            }
            init2 = init;
            init = Runtime.getRuntime().freeMemory();
            if (init == init2) ++count;
            else count = 0;
        } while (count < 4);
        return init;
    }

    abstract void run();

    public void printInfo(){
        run();
        System.out.println("#####################################################################################################################################\n");
        System.out.println("With memory difference method - " + instance.getClass().getCanonicalName() + ":\t" + memoryFormat((memoryDiff) / count));
        System.out.println("With ObjectSizeCalculator - " + instance.getClass().getCanonicalName() + ":\t" + memoryFormat(ObjectSizeCalculator.getObjectSize(instance)));
        System.out.println("With instrumentation - " + instance.getClass().getCanonicalName() + ":\t" + memoryFormat(Size.of(instance)));
        System.out.println();
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());
    }
}