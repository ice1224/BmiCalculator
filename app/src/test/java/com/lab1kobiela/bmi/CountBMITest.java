package com.lab1kobiela.bmi;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mariusz on 2017-03-23.
 */

public class CountBMITest {
    //    GIVEN
    private float testMass;
    private float testHeight;
    //    WHEN
    private ICountBMI counter = new CountBMIForKgM();
    //    THEN

    @Test
    public void mUnderZero() {
        testMass = -5.0f;
        boolean result = counter.isMassValid(testMass);
        assertFalse(result);
    }

    @Test
    public void mEqualsZero() {
        testMass = 0.0f;
        boolean result = counter.isMassValid(testMass);
        assertFalse(result);
    }

    @Test
    public void mUnderBottomLimit() {
        testMass = CountBMIForKgM.MIN_MASS - 1;
        boolean result = counter.isMassValid(testMass);
        assertFalse(result);
    }

    @Test
    public void mEqualsBottomLimit() {
        testMass = CountBMIForKgM.MIN_MASS;
        boolean result = counter.isMassValid(testMass);
        assertTrue(result);
    }

    @Test
    public void mEqualsTopLimit() {
        testMass = CountBMIForKgM.MAX_MASS;
        boolean result = counter.isMassValid(testMass);
        assertTrue(result);
    }

    @Test
    public void mAboveTopLimit() {
        testMass = CountBMIForKgM.MAX_MASS + 1;
        boolean result = counter.isMassValid(testMass);
        assertFalse(result);
    }

    @Test
    public void mCorrectValue() {
        testMass = CountBMIForKgM.MIN_MASS + 1;
        boolean result = counter.isMassValid(testMass);
        assertTrue(result);
    }


    @Test
    public void hUnderZero() {
        testHeight = -5.0f;
        boolean result = counter.isHeightValid(testHeight);
        assertFalse(result);
    }

    @Test
    public void hEqualsZero() {
        testHeight = 0.0f;
        boolean result = counter.isHeightValid(testHeight);
        assertFalse(result);
    }

    @Test
    public void hUnderBottomLimit() {
        testHeight = CountBMIForKgM.MIN_HEIGHT - 1;
        boolean result = counter.isHeightValid(testHeight);
        assertFalse(result);
    }

    @Test
    public void hEqualsBottomLimit() {
        testHeight = CountBMIForKgM.MIN_HEIGHT;
        boolean result = counter.isHeightValid(testHeight);
        assertTrue(result);
    }

    @Test
    public void hEqualsTopLimit() {
        testHeight = CountBMIForKgM.MAX_HEIGHT;
        boolean result = counter.isHeightValid(testHeight);
        assertTrue(result);
    }

    @Test
    public void hAboveTopLimit() {
        testHeight = CountBMIForKgM.MAX_HEIGHT + 1;
        boolean result = counter.isHeightValid(testHeight);
        assertFalse(result);
    }

    @Test
    public void hCorrectValue() {
        testHeight = CountBMIForKgM.MIN_HEIGHT + 1;
        boolean result = counter.isHeightValid(testHeight);
        assertTrue(result);
    }


    @Test
    public void countIsCorrect() {
        testMass = CountBMIForKgM.MAX_MASS - 1;
        testHeight = CountBMIForKgM.MAX_HEIGHT - 1;
        float expected = testMass / (testHeight * testHeight);
        float actual = counter.countBMI(testMass, testHeight);
        assertEquals(expected, actual, 2);
    }

}
