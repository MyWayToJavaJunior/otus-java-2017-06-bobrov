import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

/**
 * Created by lWeRl on 25.06.2017.
 */
public class GCLoggerDaemon extends Thread {

    private final List<GarbageCollectorMXBean> gcMXBeans;
    private final String[] gcNames;
    private final long gcCounts[];
    private final long gcDurations[];


    public GCLoggerDaemon(List<GarbageCollectorMXBean> gcMXBeans) {
        this.gcMXBeans = gcMXBeans;
        this.gcCounts = new long[gcMXBeans.size()];
        this.gcDurations = new long[gcMXBeans.size()];
        this.gcNames = new String[gcMXBeans.size()];
        for (int i = 0; i < gcMXBeans.size(); i++) {
            gcNames[i] = gcMXBeans.get(i).getName();
        }
        this.setDaemon(true);
        this.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000 * 60);
                for (int i = 0; i < gcMXBeans.size(); i++) {
                    System.out.println(gcNames[i] + ":" +
                            " count=" + (gcMXBeans.get(i).getCollectionCount() - gcCounts[i]) +
                            " time=" + (gcMXBeans.get(i).getCollectionTime() - gcDurations[i]) + "ms");

                    gcCounts[i] = gcMXBeans.get(i).getCollectionCount();
                    gcDurations[i] = gcMXBeans.get(i).getCollectionTime();

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
