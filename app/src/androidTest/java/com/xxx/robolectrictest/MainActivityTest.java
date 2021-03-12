package com.xxx.robolectrictest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Test
    public void testTextViewClickSwitchActivity() {
        try (ActivityScenario<MainActivity> activityScenario
                     = ActivityScenario.launch(MainActivity.class)) {
            //below code run on MainActivity
            onView(withId(R.id.tv1)).check(matches(withText("MainActivity")));
            onView(withId(R.id.tv1)).perform(click());

            //SecondActivity started, below code run on SecondActivity
            onView(withId(R.id.tv1)).check(matches(withText("SecondActivity")));
            onView(withId(R.id.tv1)).perform(click());

            //ThirdActivity started, below code run on ThirdActivity
            onView(withId(R.id.tv1)).check(matches(withText("ThirdActivity")));
            pressBack();

            //back to SecondActivity, below code run on SecondActivity
            onView(withId(R.id.tv1)).check(matches(withText("SecondActivity")));
            pressBack();

            //back to MainActivity
            onView(withId(R.id.tv1)).check(matches(withText("MainActivity")));
        }
    }
}