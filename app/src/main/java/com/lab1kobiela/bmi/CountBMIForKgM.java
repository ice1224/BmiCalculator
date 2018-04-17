package com.lab1kobiela.bmi;

/**
 * Created by Mariusz on 2017-03-20.
 */

public class CountBMIForKgM implements ICountBMI {

    static final float MIN_MASS = 10f;
    static final float MAX_MASS = 250f;
    static final float MIN_HEIGHT = 0.5f;
    static final float MAX_HEIGHT = 2.5f;


    public boolean isMassValid(float m){
        return m>=MIN_MASS&&m<=MAX_MASS;
    }


    public boolean isHeightValid(float h){
        return h>=MIN_HEIGHT&&h<=MAX_HEIGHT;
    }


    public float countBMI(float m, float h){
        if(!isMassValid(m)) throw new IllegalArgumentException("Wrong mass");
        if(!isHeightValid(h)) throw new IllegalArgumentException("Wrong height");
        return m/(h*h);
    }
}