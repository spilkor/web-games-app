package com.spilkor.webgamesapp.util;

import java.util.Collection;

public class MathUtil {

    private MathUtil(){};

    public static boolean coinToss(){
        return (Math.random() < 0.5);
    }

    public static boolean inRange(int min, int value, int max){
        return min <= value && value <= max;
    }

    public static int randomNumberBetween(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    public static int randomNumberLowerThan(int than) {
        return randomNumberBetween(0, than - 1);
    }

    public static <E> E selectRandom(Collection<E> elements) {
        return (E) elements.toArray()[randomNumberLowerThan(elements.size())];
    }

}
