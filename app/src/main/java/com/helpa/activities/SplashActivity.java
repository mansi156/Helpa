package com.helpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by appinventiv on 3/11/17.
 */

public class SplashActivity extends BaseActivity {
    private int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        showSplashScreen();
    }


    /*
       * method to start new activity after 2 seconds
       * */
    public void showSplashScreen() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
