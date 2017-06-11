import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.util.ArrayList;

/**
 * Created by lWeRl on 11.06.2017.
 */
public class Size {
    final static int count = 10_000; //имеются волшебные числа 100_000, 10_000_000! WTF!!!

    static String memoryFormat(long bytes) {
        int byteAmount = (int) (bytes % 1024);
        int kbAmount = (int) ((bytes / 1024) % 1024);
        int mbAmount = (int) ((bytes / (1024 * 1024)) % 1024);
        int gbAmount = (int) ((bytes / (1073741824)));
        String result = "";
        result = gbAmount == 0 ? result : result + gbAmount + "GB ";
        result = mbAmount == 0 ? result : result + mbAmount + "MB ";
        result = kbAmount == 0 ? result : result + kbAmount + "KB ";
        result = byteAmount == 0 ? result : result + byteAmount + "Bytes";
        return result;
    }

    static long getFreeMemory() {
        // waits for free memory measurement to stabilize
        long init = Runtime.getRuntime().freeMemory(), init2;
        int count = 0;
        System.out.println("waiting..." + memoryFormat(init));
        do {
            System.gc();
            try {
                Thread.sleep(100);
            } catch (Exception x) {
            }
            init2 = init;
            init = Runtime.getRuntime().freeMemory();
            if (init == init2) ++count;
            else count = 0;
        } while (count < 5);
        System.out.println("ok..." + memoryFormat(init));
        return init;
    }

    public static void main(String[] args) throws InterruptedException {
        new Size().run();
    }

    void run() throws InterruptedException {

        ArrayList<Integer> temp = new ArrayList<>();
        //temp.add(1000);
        //temp.add(1000);
        //temp.add(1000);

        // Предворительная очистка
        getFreeMemory();

        Object[] s = new Object[count];

        long init = getFreeMemory();

        for (int j = 0; j < count; ++j)
            s[j] = new ArrayList<>();

        long afters = getFreeMemory();

        System.out.println(s[0].getClass().getCanonicalName() + ":\t" + memoryFormat((init - afters) / count));
        System.out.println(s[0].getClass().getCanonicalName() + ":\t" + memoryFormat(ObjectSizeCalculator.getObjectSize(s[0])));
    }

}
