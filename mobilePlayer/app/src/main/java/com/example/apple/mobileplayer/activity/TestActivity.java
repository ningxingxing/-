package com.example.apple.mobileplayer.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.apple.mobileplayer.base.BaseActivity;

/**
 * Created by apple on 17/2/9.
 */
public class TestActivity extends BaseActivity{

    private final String TAG = "TestActivity";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.e(TAG,"onCreateT");

        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        Log.e(TAG,"onStartT");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.e(TAG,"onRestartT");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG,"onStopT");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e(TAG,"onPauseT");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"onDestroyT");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.e(TAG,"onResumeT");
        super.onResume();
    }
}
