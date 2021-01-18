package me.dkim19375.dkim19375core.function;

import java.util.Map;

public final class Entry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Entry(Entry<K, V> entry) {
        key = entry.getKey();
        value = entry.getValue();
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    public K setKey(K key) {
        K old = this.key;
        this.key = key;
        return old;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    public Entry<K, V> setValues(K key, V value) {
        Entry<K, V> e = new Entry<>(this.key, this.value);
        this.key = key;
        this.value = value;
        return e;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}