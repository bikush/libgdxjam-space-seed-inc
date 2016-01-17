package com.starseed.util;

import java.util.Random;

public class RandomUtils {
	
	private static Random rand = new Random();

	public static float rangeFloat( float min, float max ){
		return rand.nextFloat() * (max - min) + min;
	}
	
	public static float nextFloat(){
		return rand.nextFloat();
	}
	
	/**
	 * Produce a random integer within the min/max. Borders are inclusive.
	 * @param min Lower limit.
	 * @param max Upper limit.
	 * @return Random integer between the given limits.
	 */
	public static int rangeInt( int min, int max ){
		return rand.nextInt(max - min + 1) + min;
	}
	
	public static int nextInt(){
		return rand.nextInt();
	}
	
	public static int nextInt( int n ){
		return rand.nextInt(n);
	}

    /**
     * @see [Stack Overflow](http://stackoverflow.com/a/1973018)
     * @param <E>
     */
    @SuppressWarnings("rawtypes")
	public static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }
    
}
