package com.lwerl.cache;

import java.util.TimerTask;

@SuppressWarnings("WeakerAccess")
public class MyElement<K, V> {
    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;
    private TimerTask idleTimer;

    public MyElement(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    public TimerTask getIdleTimer() {
        return idleTimer;
    }

    public void setIdleTimer(TimerTask idleTimer) {
        this.idleTimer = idleTimer;
    }
}
