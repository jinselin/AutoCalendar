package com.five.high.emirim.testintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG ="오캘:MainActivity";
    private ImageView button1;
    private ImageView button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate 실행");
        button1 = (ImageView) findViewById(R.id.nextBtn1);
        button2 = (ImageView) findViewById(R.id.calBtn);
        Log.e(TAG, "이미지뷰 버튼 가져옴");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Log.e(TAG, "onClick 실행");
        switch (view.getId()) {
            case R.id.nextBtn1:
                Log.e(TAG, "nextBtn 눌렸네" );
                // source code
                intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
                Toast.makeText(this,"복붙 화면으로 넘어갑니다.",Toast.LENGTH_LONG).show();
                break;
            case R.id.calBtn:
                Log.e(TAG, "calBtn 눌렸네");
                intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                Toast.makeText(this,"달력 화면으로 넘어갑니다.",Toast.LENGTH_LONG).show();
                break;
        }
    }
}

