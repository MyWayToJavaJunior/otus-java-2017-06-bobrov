package org.lwerl.objectsize;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.lwerl.sizeof.Size;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.function.Supplier;

/**
 * Created by lWeRl on 11.06.2017.
 */
public class InstanceSize {
    private final int count = 10_000;
    private long memoryDiff;
    private Supplier supplier;

    static {
        System.out.println(VM.current().details());
    }

    private static String memoryFormat(long bytes) {
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

    private static long getFreeMemory() {
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

    private void run() {
        getFreeMemory();
        Object[] s = new Object[count];
        long init = getFreeMemory();
        for (int j = 0; j < count; ++j) {
            s[j] = supplier.get();
        }
        memoryDiff = init - getFreeMemory();
    }

    InstanceSize(Supplier supplier) {
        this.supplier = supplier;
    }


    void printInfo(){
        run();
        String className = supplier.get().getClass().getCanonicalName();
        System.out.println("###################################################################" +
                "##################################################################\n");
        System.out.println("With memory difference method - " + className + ":\t"
                + memoryFormat((memoryDiff) / count));
        System.out.println("With ObjectSizeCalculator - " + className + ":\t"
                + memoryFormat(ObjectSizeCalculator.getObjectSize(supplier.get())));
        System.out.println("With instrumentation - " + className + ":\t"
                + memoryFormat(Size.of(supplier.get())));
        System.out.println();
        System.out.println(ClassLayout.parseInstance(supplier.get()).toPrintable());
    }
}