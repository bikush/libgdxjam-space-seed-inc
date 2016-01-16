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
    
    @Override
    public int hashCode() {
    	return (first.hashCode() ^ second.hashCode());
    }
    
    @Override
    public boolean equals(Object obj) {
    	if( obj != null &&  obj instanceof Pair<?,?> ){
    		 Pair<?,?> otherPair = (Pair<?,?>)obj;
    		 return otherPair.first.equals(first) && otherPair.second.equals(second) ||
    				 otherPair.second.equals(first) && otherPair.first.equals(second);
    	}
    	return false;
    }

}