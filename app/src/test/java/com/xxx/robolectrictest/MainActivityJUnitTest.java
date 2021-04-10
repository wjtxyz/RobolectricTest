package com.xxx.robolectrictest;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.testng.reporters.jq.Main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class MainActivityJUnitTest {
    @Test
    public void testLogd(){
        Log.d("TTTT", "YYYY");
    }

    @Test
    public void testEditMapNameDialogFragment(){
        assertNull(MainActivity.ExcludeIllegalCharInputFilter.
                filter("12你我abc_", 0, 8, null, 0, 0));
        assertEquals(MainActivity.ExcludeIllegalCharInputFilter.
                filter("12#$你我にほ",0,8, null, 0, 0), "12你我");
    }
}