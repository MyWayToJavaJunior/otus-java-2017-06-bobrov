/**
 * Created by lWeRl on 24.06.2017.
 */
public class MemoryLeakNew implements MemoryLeakMBean {


    private static final int GROW_SIZE = 64;

    private Object[] arrayForLeak = new Object[256 * 1024 * 32];

    private volatile long iterationCount = 1;
    private volatile int arrayLength = GROW_SIZE;

    private volatile int growSizeMultiplier = 8;

    private volatile boolean oldGenPollution = false;
    private volatile int oldGenPollutionDivider = 250;

    {
        for (int i = 0; i < GROW_SIZE; i++) {
            arrayForLeak[i] = new Object();
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

            // pollute young gen
            int offsetIndex = arrayLength / 2;
            arrayLength += GROW_SIZE * growSizeMultiplier;
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
