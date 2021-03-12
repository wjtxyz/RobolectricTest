package com.xxx.robolectrictest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

@SuppressLint("HandlerLeak")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv1).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SecondActivity.class)));
        findViewById(R.id.tv2).setOnClickListener(v -> new CountDownDialogFragment().show(getSupportFragmentManager(), null));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }


    public static class CountDownDialogFragment extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_countdown, container, false);
        }

        final CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 * 10, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getView().<TextView>findViewById(R.id.tv1).setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                dismissAllowingStateLoss();
            }
        };

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            countDownTimer.start();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            countDownTimer.cancel();
        }
    }
}
