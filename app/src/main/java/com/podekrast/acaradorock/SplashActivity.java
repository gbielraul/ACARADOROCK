package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private ImageView imgAcdr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        imgAcdr = findViewById(R.id.img_splash);

        Handler handler = new Handler();
        handler.postDelayed(this, 1500);
    }

    @Override
    public void run() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);

        startActivity(intent);
        finish();
    }
}
