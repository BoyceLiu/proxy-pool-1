package com.boyceliu.proxypool.store;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectStore<K, V> {
    private static ObjectStore instance = null;
    private static final Map store = new ConcurrentHashMap<>();

    private ObjectStore() {}

    public static ObjectStore getInstance() {
        if (instance == null) {
            instance = new ObjectStore();
        }
        return instance;
    }

    public void put(K key, V v) {
        store.put(key, v);
    }

    public V get(K key) {
        return (V) store.get(key);
    }

    public List<V> getAll() {
        return ImmutableList.copyOf(store.values());
    }

    public int size() {
        return store.size();
    }

    public void reomve(K key) {
        store.remove(key);
    }
}
