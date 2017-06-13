package org.lwerl.objectsize;

/**
 * Created by lWeRl on 12.06.2017.
 * First install sizeof project
 * Start JVM with -javaagent:sizeof/target/sizeof.jar option
 */
public class Main {
    public static void main(String[] args) {
//        new InstanceSize(Object::new).printInfo();
//        new InstanceSize(() -> new String(new char[0])).printInfo();
//        new InstanceSize(() -> new String(new char[]{'t', 'e', 's', 't', '!'})).printInfo();
//        new InstanceSize(() -> new int[0]).printInfo();
//        new InstanceSize(() -> new int[100]).printInfo();
//        new InstanceSize(() -> new int[1000]).printInfo();
        new InstanceSize(MyClass::new).printInfo();
    }

    private static class MyClass {
        int a;
        boolean b;
        String s;
    }
}
