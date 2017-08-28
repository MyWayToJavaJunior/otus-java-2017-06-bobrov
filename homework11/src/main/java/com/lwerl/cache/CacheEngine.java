package com.lwerl.cache;

public interface CacheEngine<K, V> {

    void put(MyElement<K, V> element);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

    void evict(K key);
}
