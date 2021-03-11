package com.xxx.robolectrictest;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("HandlerLeak")
public class BindServiceActivity extends AppCompatActivity {
    private static final String TAG = "BindServiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);
        bindService(new Intent().setComponent(new ComponentName("com.xxx.servicetest", "com.xxx.servicetest.TestService")), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    final Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
            super.handleMessage(msg);
        }
    };

    final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            Messenger messengerServer = new Messenger(service);
            Message message = Message.obtain(null, 1, 2, 3);
            message.replyTo = new Messenger(mUIHandler);
            try {
                messengerServer.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    };
}
