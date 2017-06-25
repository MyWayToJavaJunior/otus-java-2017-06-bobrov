import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by lWeRl on 24.06.2017.
 */
public class Main {
    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.lwerl:type=MemoryLeak");
        MemoryLeak mbean = new MemoryLeak();
        mbs.registerMBean(mbean, name);

        new GCLoggerDaemon(ManagementFactory.getGarbageCollectorMXBeans());
        mbean.leak();
    }
}
