/**
 * Created by lWeRl on 12.06.2017.
 */
import java.lang.instrument.Instrumentation;

public class Size {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long of(Object o) {
        return instrumentation.getObjectSize(o);
    }
}
