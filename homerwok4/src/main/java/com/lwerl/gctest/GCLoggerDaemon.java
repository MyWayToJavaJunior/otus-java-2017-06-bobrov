package com.lwerl.gctest;

import javax.management.*;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Created by lWeRl on 25.06.2017.
 */
public class GCLoggerDaemon extends Thread {

    private final List<GarbageCollectorMXBean> gcMXBeans;
    private final String[] gcNames;
    private final long gcCounts[];
    private final long gcDurations[];
    private final ObjectName mName;


    public GCLoggerDaemon(List<GarbageCollectorMXBean> gcMXBeans, ObjectName mName) {
        this.mName = mName;
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
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            while (true) {
                Thread.sleep(1000 * 60);
                Long iteration = (Long) mbs.getAttribute(mName, "IterationCount");
                for (int i = 0; i < gcMXBeans.size(); i++) {
                    System.out.println(gcNames[i] + ":" +
                            " count=" + (gcMXBeans.get(i).getCollectionCount() - gcCounts[i]) +
                            " time=" + (gcMXBeans.get(i).getCollectionTime() - gcDurations[i]) + "ms");

                    gcCounts[i] = gcMXBeans.get(i).getCollectionCount();
                    gcDurations[i] = gcMXBeans.get(i).getCollectionTime();

                }
                System.out.println("iteration=" + iteration);
            }
        } catch (InterruptedException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | MBeanException e) {
            e.printStackTrace();
        }
    }
}
