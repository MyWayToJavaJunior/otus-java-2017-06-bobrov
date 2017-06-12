import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * Created by lWeRl on 11.06.2017.
 * First install sizeof project
 * Start JVM with -javaagent:sizeof/target/sizeof.jar option
 */
public class ObjectSize {
    final private static int count = 100_000;

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

    public static void main(String[] args) throws InterruptedException {
        new ObjectSize().run();
    }

    void run() throws InterruptedException {
        getFreeMemory();
        Object[] s = new Object[count];
        long init = getFreeMemory();
        for (int j = 0; j < count; ++j) {
//            s[j] = new ArrayList<>();
//            s[j] = new String(new char[]{});
//            s[j] = new Object();
//            s[j] = new Object[1];
            s[j] = new MyClass();
//            s[j] = new byte[1024 - 16];
//            s[j] = new Integer(200);
        }
        long afters = getFreeMemory();

        System.out.println("With memory difference method - " + s[0].getClass().getCanonicalName() + ":\t" + memoryFormat((init - afters) / count));
        System.out.println("With ObjectSizeCalculator - " + s[0].getClass().getCanonicalName() + ":\t" + memoryFormat(ObjectSizeCalculator.getObjectSize(s[0])));
        System.out.println("With instrumentation - " + s[0].getClass().getCanonicalName() + ":\t" + memoryFormat(Size.of(s[0])));
        System.out.println();
        System.out.println(ClassLayout.parseInstance(s[0]).toPrintable());
    }

}