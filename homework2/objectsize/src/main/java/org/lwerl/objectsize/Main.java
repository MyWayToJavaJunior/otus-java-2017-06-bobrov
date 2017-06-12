package org.lwerl.objectsize;

import org.lwerl.objectsize.factory.InstanceSizeFactory;

import java.util.function.Supplier;

/**
 * Created by lWeRl on 12.06.2017.
 * First install sizeof project
 * Start JVM with -javaagent:sizeof/target/sizeof.jar option
 */
public class Main {
    public static void main(String[] args) {
        Supplier<InstanceSizeFactory> instanceSizeFactory = InstanceSizeFactory::new;
        instanceSizeFactory.get().getInstanceSize("OBJECT").printInfo();
//        instanceSizeFactory.get().getInstanceSize("STRING_EMPTY").printInfo();
//        instanceSizeFactory.get().getInstanceSize("STRING_5_LETTERS").printInfo();
//        instanceSizeFactory.get().getInstanceSize("ARRAYLIST").printInfo();
//        instanceSizeFactory.get().getInstanceSize("ARRAY").printInfo();
//        instanceSizeFactory.get().getInstanceSize("ARRAY_ZERO_LENGTH").printInfo();
//        instanceSizeFactory.get().getInstanceSize("MYCLASS").printInfo();
    }
}
