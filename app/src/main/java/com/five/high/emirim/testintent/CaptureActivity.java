package com.five.high.emirim.testintent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        container = (LinearLayout) findViewById(R.id.container);

    }

    @Override
    public void onClick(View view) {
        container.buildDrawingCache();

        Bitmap captureView = container.getDrawingCache();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"" +
                    "/capture.jpg");
            captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Captured!", Toast.LENGTH_LONG).show();
    }
}
