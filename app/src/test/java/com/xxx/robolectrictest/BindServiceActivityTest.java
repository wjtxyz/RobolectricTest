package com.xxx.robolectrictest;

import android.app.Application;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class BindServiceActivityTest {
    @Test
    public void testBindService() {
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
        ShadowApplication shadowApplication = Shadows.shadowOf(ApplicationProvider.<Application>getApplicationContext());

        //build Service for current test on air
        shadowApplication.setComponentNameAndServiceForBindService(
                new ComponentName("com.xxx.servicetest", "com.xxx.servicetest.Test"), new Messenger(handler).getBinder());

        try (ActivityScenario<BindServiceActivity> activityScenario = ActivityScenario.launch(BindServiceActivity.class)) {
            System.out.println("succeed to launch BindServiceActivity succeed");
        }
    }
}