import java.util.Arrays;
import java.util.Random;

/**
 * Created by lWeRl on 16.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        int[] a1 = new int[10_000_000];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(Integer.MAX_VALUE);
        }
        int[] a2 = Arrays.copyOf(a1, a1.length);

        long start;

        start = System.nanoTime();
        SortHelper.quadSort(a1);
        System.out.println("Time: " + (System.nanoTime() - start));

        start = System.nanoTime();
        Arrays.sort(a2);
        System.out.println("Time: " + (System.nanoTime()- start));

    }
}
