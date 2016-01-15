package com.starseed.util;

public class Pair<K, V> {

    public final K first;
    public final V second;

    public static <K, V> Pair<K, V> createPair(K first, V second) {
        return new Pair<K, V>(first, second);
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

}