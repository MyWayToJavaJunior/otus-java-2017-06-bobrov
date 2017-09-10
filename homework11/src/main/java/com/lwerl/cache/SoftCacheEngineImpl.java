package com.lwerl.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class SoftCacheEngineImpl<K, V> implements CacheEngine<K, V>, CacheStatistic {

    private static final int TIME_THRESHOLD_MS = 1;

    private final int maxSize;
    private final long idleTime;
    private final long lifeTime;
    private final boolean isEternal;

    private final Map<K, SoftReference<MyElement<K, V>>> cache = new LinkedHashMap<>();
    private final Timer timer = new Timer(true);

    private int hit;
    private int miss;

    public SoftCacheEngineImpl(int maxSize, long idleTime, long lifeTime) {
        this.maxSize = maxSize > 0 ? maxSize : Short.MAX_VALUE;
        this.idleTime = idleTime > 0 ? idleTime : 0;
        this.lifeTime = lifeTime > 0 ? lifeTime : 0;
        this.isEternal = this.idleTime == 0 && this.lifeTime == 0;
        if (idleTime > 0) {
            addTimerPurging(idleTime);
        }
    }

    @Override
    public void put(MyElement<K, V> element) {

        if (cache.size() == maxSize) {
            K firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        K key = element.getKey();
        cache.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTime != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTime);
                timer.schedule(lifeTimerTask, lifeTime);
            }
            if (idleTime != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTime);
                element.setIdleTimer(idleTimerTask);
                timer.schedule(idleTimerTask, idleTime);
            }
        }
    }

    @Override
    public V get(K key) {
        SoftReference<MyElement<K, V>> reference = cache.get(key);

        if (reference == null) {
            miss++;
            return null;
        }

        MyElement<K, V> element = reference.get();

        if (element == null) {
            miss++;
            cache.remove(key);
            return null;
        } else {
            hit++;
            element.setAccessed();
            if (idleTime != 0) {
                element.getIdleTimer().cancel();
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTime);
                element.setIdleTimer(idleTimerTask);
                timer.schedule(idleTimerTask, idleTime);
            }
            return element.getValue();
        }
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    @Override
    public void evict(K key) {
        cache.remove(key);
    }

    @Override
    public CacheStatistic getStatistic() {
        return this;
    }

    private void addTimerPurging(long idleTime) {
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timer.purge();
            }
        }, idleTime, idleTime);
    }

    private TimerTask getTimerTask(final K key, Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<MyElement<K, V>> element = cache.get(key);
                boolean hasValue = element != null && element.get() != null;
                if (!hasValue ||
                        isT1BeforeT2(timeFunction.apply(element.get()), System.currentTimeMillis())) {
                    cache.remove(key);
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public long getIdleTime() {
        return idleTime;
    }

    @Override
    public long getLifeTime() {
        return lifeTime;
    }

    @Override
    public boolean isEternal() {
        return isEternal;
    }

    @Override
    public int getSize() {
        return cache.size();
    }


}
