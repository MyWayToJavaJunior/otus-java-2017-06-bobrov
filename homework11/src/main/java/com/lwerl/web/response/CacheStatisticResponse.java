package com.lwerl.web.response;

import com.lwerl.cache.CacheStatistic;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class CacheStatisticResponse {

    public static CacheStatisticResponse getStatistic(CacheStatistic statistic) {
        CacheStatisticResponse result = new CacheStatisticResponse();

        result.miss = statistic.getMissCount();
        result.hit = statistic.getHitCount();
        result.size = statistic.getSize();
        result.maxSize = statistic.getMaxSize();
        result.idleTime = statistic.getIdleTime();
        result.lifeTime = statistic.getLifeTime();
        result.isEternal = statistic.isEternal();

        return result;
    }

    public int miss;
    public int hit;
    public int size;
    public int maxSize;
    public long idleTime;
    public long lifeTime;
    public boolean isEternal;

}
