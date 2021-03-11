package com.xxx.robolectrictest;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityJUnitTest {
    @Test
    public void testLogd(){
        Log.d("TTTT", "YYYY");
    }
}