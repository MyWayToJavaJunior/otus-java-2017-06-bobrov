import java.util.Arrays;
import java.util.Random;

/**
 * Created by lWeRl on 16.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        // -Xmx10g -Xms10g
        // ~25s / ~70s
//        int length = 500_000_000;
        int length = 20;
        int[] a1 = new int[length];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(Integer.MAX_VALUE);
        }
        int[] a2 = Arrays.copyOf(a1, a1.length);

        System.out.println("Generation complete. Length = " + length);
        long start;

        start = System.nanoTime();
        SortHelper.quadSort(a1);
        System.out.println("Time: " + (System.nanoTime() - start)+ "ns | " + (System.nanoTime() - start)/1_000_000 + "ms");
        if (length < 21) {
            System.out.println(Arrays.toString(a1));
        }

        start = System.nanoTime();
        Arrays.sort(a2);
        System.out.println("Time: " + (System.nanoTime() - start)+ "ns | " + (System.nanoTime() - start)/1_000_000 + "ms");
        if (length < 21) {
            System.out.println(Arrays.toString(a2));
        }
    }
}
