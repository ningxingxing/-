package com.example.apple.mobileplayer.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //两秒后执行

                //执行在主线程中
                startMainActivity();
                Log.e(TAG,"当前activity="+TAG);

            }
        },2000);
    }

    private void startMainActivity() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
