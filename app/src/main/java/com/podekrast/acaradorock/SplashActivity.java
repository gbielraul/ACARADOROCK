package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*------*/
        Handler handler = new Handler();
        handler.postDelayed(this, 1500);
    }

    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        finish();
    }
}
