package com.spilkor.webgamesapp.util;

public class MathUtil {

    private MathUtil(){};

    public static boolean coinToss(){
        return (Math.random() < 0.5);
    }

    public static boolean inRange(int min, int value, int max){
        return min <= value && value <= max;
    }

}
