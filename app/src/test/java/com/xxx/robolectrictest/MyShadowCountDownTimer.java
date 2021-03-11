package com.xxx.robolectrictest;

import android.os.CountDownTimer;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

@Implements(CountDownTimer.class)
public class MyShadowCountDownTimer {
    @RealObject
    CountDownTimer realObject;

    //just for test, actually can comment this method
    @Implementation
    protected CountDownTimer start() {
        return Shadow.directlyOn(realObject, CountDownTimer.class, "start");
    }
}