package com.lwerl.gctest;

/**
 * Created by lWeRl on 24.06.2017.
 */
public interface MemoryLeakMBean {
    int getGrowSize();

    long getIterationCount();

    int getArrayLength();

    int getGrowSizeMultiplier();

    void setGrowSizeMultiplier(int growSizeMultiplier);

    boolean isOldGenPollution();

    void setOldGenPollution(boolean oldGenPollution);

    int getOldGenPollutionDivider();

    void setOldGenPollutionDivider(int oldGenPollutionDivider);

}
