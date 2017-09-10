package com.lwerl.cache;

public interface CacheEngine<K, V> {

    void put(MyElement<K, V> element);

    V get(K key);

    void dispose();

    void evict(K key);
}
