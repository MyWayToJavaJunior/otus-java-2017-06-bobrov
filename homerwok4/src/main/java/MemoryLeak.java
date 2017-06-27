import java.util.ArrayList;

/**
 * Created by lWeRl on 24.06.2017.
 */
public class MemoryLeak implements MemoryLeakMBean {
    private static final int GROW_SIZE = 1024;

    private ArrayList<Object> listForLeak = new ArrayList<>();

    private volatile long iterationCount = 1;

    private volatile int growSizeMultiplier = 1;
    private Object[] arrayForLeak = new Object[GROW_SIZE * growSizeMultiplier];
    private volatile int arrayLength = arrayForLeak.length;

    private volatile boolean oldGenPollution = false;
    private volatile int oldGenPollutionDivider = 100;

    {
        for (int i = 0; i < GROW_SIZE * growSizeMultiplier; i++) {
            listForLeak.add(new Object());
        }
    }

    @Override
    public int getGrowSize() {
        return GROW_SIZE;
    }

    @Override
    public long getIterationCount() {
        return iterationCount;
    }

    @Override
    public int getArrayLength() {
        return arrayLength;
    }

    @Override
    public int getGrowSizeMultiplier() {
        return growSizeMultiplier;
    }

    @Override
    public void setGrowSizeMultiplier(int growSizeMultiplier) {
        this.growSizeMultiplier = growSizeMultiplier;
    }

    @Override
    public boolean isOldGenPollution() {
        return oldGenPollution;
    }

    @Override
    public void setOldGenPollution(boolean oldGenPollution) {
        this.oldGenPollution = oldGenPollution;
    }

    @Override
    public int getOldGenPollutionDivider() {
        return oldGenPollutionDivider;
    }

    @Override
    public void setOldGenPollutionDivider(int oldGenPollutionDivider) {
        this.oldGenPollutionDivider = oldGenPollutionDivider;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void leak() {
        while (true) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // pollute young gen
            int offsetIndex = arrayLength / 2;
            Object[] newArray = new Object[arrayLength + GROW_SIZE * growSizeMultiplier];
            System.arraycopy(arrayForLeak, 0, newArray, 0, offsetIndex);
            arrayForLeak = newArray;
            arrayLength = arrayForLeak.length;
            for (int i = offsetIndex; i < arrayLength; i++) {
                arrayForLeak[i] = new Object();
            }

            // pollute old gen
            if (oldGenPollution && (iterationCount % oldGenPollutionDivider == 0)) {
                for (int i = 0; i < arrayLength; i++) {
                    arrayForLeak[i] = new Object();
                }
            }

            iterationCount++;
        }
    }
}
