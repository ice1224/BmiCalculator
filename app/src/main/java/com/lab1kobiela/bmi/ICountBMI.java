package com.lab1kobiela.bmi;

/**
 * Created by Mariusz on 2017-03-20.
 */

public interface ICountBMI {
    boolean isMassValid(float m);
    boolean isHeightValid(float h);
    float countBMI(float m, float h);
}
