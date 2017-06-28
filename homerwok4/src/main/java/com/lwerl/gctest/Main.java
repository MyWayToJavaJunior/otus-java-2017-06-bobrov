package com.lwerl.gctest;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by lWeRl on 24.06.2017.
 */
public class Main {
    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        // Общее
        // -Xmx64m -Xms64m
        // GC на выбор
        // -XX:+UseSerialGC
        // -XX:+UseParallelGC -XX:+UseParallelOldGC
        // -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseGCOverheadLimit
        // -XX:+UseG1GC - очень сложно заставить умереть процесс ~12 минут держиться, в основном безполезную работу делает)))

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.lwerl.gctest:type=MemoryLeak");
        MemoryLeak mbean = new MemoryLeak();
        mbs.registerMBean(mbean, name);

        new GCLoggerDaemon(ManagementFactory.getGarbageCollectorMXBeans(), name);
        mbean.leak();
    }
}
