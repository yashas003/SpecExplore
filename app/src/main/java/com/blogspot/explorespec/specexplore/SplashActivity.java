package com.blogspot.explorespec.specexplore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }
}
