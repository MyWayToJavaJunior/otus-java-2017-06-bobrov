package com.lwerl.cache;

/**
 * Created by lWeRl on 04.09.2017.
 */
public interface CacheStatistic {
    int getHitCount();

    int getMissCount();

    int getSize();

    int getMaxSize();

    long getIdleTime();

    long getLifeTime();

    boolean isEternal();
}
