package com.five.high.emirim.testintent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, NewcalendarActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
