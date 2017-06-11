/**
 * Created by lWeRl on 11.06.2017.
 */
public class Main {
    void run() {
        System.out.println(Runtime.getRuntime().freeMemory());
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();
        System.out.println(Runtime.getRuntime().freeMemory());
    }
    public static void main(String[] args) {
        new Main().run();
    }
}
