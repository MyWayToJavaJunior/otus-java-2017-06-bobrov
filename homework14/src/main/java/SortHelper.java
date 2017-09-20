import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lWeRl on 16.09.2017.
 */
public class SortHelper {

    public static void quadSort(int[] array) {
        new SortTask(array, 4).invoke();
    }


    private static int[] getFirstHalf(int[] source) {
        int[] result = new int[source.length / 2];
        System.arraycopy(source, 0, result, 0, source.length / 2);
        return result;
    }

    private static int[] getSecondHalf(int[] source) {
        int[] result = new int[source.length - source.length / 2];
        System.arraycopy(source, source.length / 2, result, 0, source.length - source.length / 2);
        return result;
    }

    private static void merge(int[] piece1, int[] piece2, int[] target) {
        int index = 0;
        int index1 = 0;
        int index2 = 0;

        while (index < target.length) {

            boolean isBreak = false;

            if (index1 == piece1.length) {
                System.arraycopy(piece2, index2, target, index, piece2.length - index2);
                isBreak = true;
            }
            if (index2 == piece2.length) {
                System.arraycopy(piece1, index1, target, index, piece1.length - index1);
                isBreak = true;
            }

            if (isBreak) {
                break;
            }

            if (piece1[index1] < piece2[index2]) {
                target[index] = piece1[index1];
                index1++;
            } else {
                target[index] = piece2[index2];
                index2++;
            }

            index++;
        }
    }

    private static class SortTask extends RecursiveTask<int[]> {

        private int[] piece;
        private AtomicInteger count;
        private int workThread;

        public SortTask(int[] piece, AtomicInteger count, int workThread) {
            this.piece = piece;
            this.count = count;
            this.workThread = workThread;
        }

        public SortTask(int[] piece, int workThread) {
            this.piece = piece;
            this.workThread = workThread;
            this.count = new AtomicInteger(0);
        }

        @Override
        protected int[] compute() {

            if (count.get() == workThread - 1 || piece.length <= 2) {
                Arrays.sort(piece);
                return piece;
            } else {
                count.incrementAndGet();

                SortTask subTask1 = new SortTask(getFirstHalf(piece), count, workThread);
                SortTask subTask2 = new SortTask(getSecondHalf(piece), count, workThread);

                ForkJoinTask.invokeAll(subTask1, subTask2);

                int[] piece2 = subTask2.join();
                int[] piece1 = subTask1.join();

                merge(piece1, piece2, piece);

                return piece;
            }
        }
    }
}
