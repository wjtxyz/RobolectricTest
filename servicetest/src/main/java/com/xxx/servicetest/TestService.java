package com.xxx.servicetest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

@SuppressLint("HandlerLeak")
public class TestService extends Service {
    private static final String TAG = "TestService";

    public TestService() {
    }

    final Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
            try {
                msg.replyTo.send(Message.obtain(null, msg.what + 100, msg.arg1, msg.arg2));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(mUIHandler).getBinder();
    }
}
