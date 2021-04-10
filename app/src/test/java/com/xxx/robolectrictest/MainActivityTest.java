package com.xxx.robolectrictest;

import android.app.Application;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(shadows = {MyShadowCountDownTimer.class})
public class MainActivityTest {
    @Test
    public void testTextViewClickSwitchActivity() {
        try (ActivityScenario<MainActivity> activityScenario
                     = ActivityScenario.launch(MainActivity.class)) {
            activityScenario.onActivity(activity -> {
                onView(withId(R.id.tv1)).perform(click());
                assertEquals(new ComponentName(activity, SecondActivity.class.getName()),
                        shadowOf(activity)
                                .getNextStartedActivity().getComponent());
            });
        }
    }

    @Test
    public void testHandler() {
        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("1000*60*10");
            }
        }, 1000 * 60 * 10);
        RuntimeEnvironment.getMasterScheduler().advanceBy(1000 * 60 * 10, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSystemClock() {
        System.out.println("SystemClock.uptimeMillis()=" + SystemClock.uptimeMillis() + " + System.currentTimeMillis()=" + System.currentTimeMillis() + " + System.nanoTime()=" + System.nanoTime() + " + new Date()=" + new Date());
        RuntimeEnvironment.getMasterScheduler().advanceBy(1000 * 60 * 60, TimeUnit.MILLISECONDS);
        System.out.println("SystemClock.uptimeMillis()=" + SystemClock.uptimeMillis() + " + System.currentTimeMillis()=" + System.currentTimeMillis() + " + System.nanoTime()=" + System.nanoTime() + " + new Date()=" + new Date());
    }

    @Test
    public void testCountDownDialogFragment() {
        //begin test
        ActivityController<FragmentActivity> activityController = Robolectric.buildActivity(FragmentActivity.class).create().start().resume();
        new MainActivity.CountDownDialogFragment()
                .show(activityController.get().getSupportFragmentManager(), "test");
        assertNull(activityController.get().getSupportFragmentManager().findFragmentByTag("test"));

        //scheduler advance
        RuntimeEnvironment.getMasterScheduler().advanceBy(1000 * 60 * 10 - 1, TimeUnit.MILLISECONDS);
        assertNotNull(activityController.get().getSupportFragmentManager().findFragmentByTag("test"));

        RuntimeEnvironment.getMasterScheduler().advanceBy(1, TimeUnit.MILLISECONDS);
        assertNull(activityController.get().getSupportFragmentManager().findFragmentByTag("test"));

        activityController.pause().stop().destroy();
        //end test
    }

    @Test
    public void testFullyControlLifecycle() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                    case 2:
                        System.out.println("receive msg.what=" + msg.what);
                        assertNotNull(msg.replyTo);
                        try {
                            msg.replyTo.send(Message.obtain(null, msg.what + 200));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        ShadowApplication shadowApplication = shadowOf(ApplicationProvider.<Application>getApplicationContext());
        //build Activity for current test on air
        Robolectric.buildActivity(FragmentActivity.class);

        //build bindable Service for current test on air
        shadowApplication.setComponentNameAndServiceForBindService(
                new ComponentName("com.xxx.servicetest", "com.xxx.servicetest.Test"), new Messenger(handler).getBinder());

        //build ContentProvider for current test on air
        Robolectric.buildContentProvider(FakeContentProvider.class);


        ActivityController<MainActivity> mainActivityActivityController = Robolectric.buildActivity(MainActivity.class);

        //check something after Activiyt::onCreate
        assertEquals(Lifecycle.State.CREATED, mainActivityActivityController.create().get().getLifecycle().getCurrentState());

        //check something after activty::onStart
        assertEquals(Lifecycle.State.STARTED, mainActivityActivityController.start().get().getLifecycle().getCurrentState());

        //check something after activty::onResume
        assertEquals(Lifecycle.State.RESUMED, mainActivityActivityController.resume().get().getLifecycle().getCurrentState());

        //check something after activty::onPause
        assertEquals(Lifecycle.State.STARTED, mainActivityActivityController.pause().get().getLifecycle().getCurrentState());

        //check something after activty::onStop
        assertEquals(Lifecycle.State.CREATED, mainActivityActivityController.stop().get().getLifecycle().getCurrentState());

        //check something after activty::onDestroy
        assertEquals(Lifecycle.State.DESTROYED, mainActivityActivityController.destroy().get().getLifecycle().getCurrentState());

        //check something after configuration change ( screen rotate from portrait to landscape)
        RuntimeEnvironment.setQualifiers("+land");
        mainActivityActivityController.configurationChange();
    }


    @Test
    public void testEditMapNameDialogFragment() {
        ActivityController<FragmentActivity> activityController
                = Robolectric.buildActivity(FragmentActivity.class).create().start().resume();
        new MainActivity.EditMapNameDialogFragment()
                .showNow(activityController.get().getSupportFragmentManager(), "test");

        onView(withId(R.id.tv1)).inRoot(isDialog())
                .perform(replaceText("12你我abc_")).check(matches(withText("12你我abc_")));
        onView(withId(R.id.tv1)).inRoot(isDialog())
                .perform(replaceText("12#$你我にほ")).check(matches(withText("12你我")));
        onView(withId(R.id.tv1)).inRoot(isDialog())
                .perform(replaceText("12345678901234")).check(matches(withText("1234567890")));

        activityController.pause().stop().destroy();
    }
}