package com.five.high.emirim.testintent;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends Activity {
    TextView copyedit;
    TextView textview;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_next2);
                copyedit = (TextView)findViewById(R.id.cntrcv);
        }

        public void OnClick(View v) {
                switch (v.getId()) {
                        case R.id.cntrcv:
                                copyText();
                                break;
                        case R.id.nextBtn2:
                        Intent intent = new Intent(FirstActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        Toast.makeText(this,"달력 화면으로 넘어갑니다.",Toast.LENGTH_LONG).show();
                        break;
                }
        }

        void copyText() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = cm.getPrimaryClip();
                ClipData.Item item = clip.getItemAt(0);

                // TODO : item d에서 텍스트 읽어오기

                String text =  item.getText().toString(); //copyedit.getText().toString();

                copyedit.setText(text);
               // text = processText(text);
                // 텍스트 가공

                if (text.length() != 0) {
                        //
                        clip = ClipData.newPlainText("text", text);

                        cm.setPrimaryClip(clip);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                       copyedit.setText(text);
                }
        }

       // private String processText(String text) {
                // TODO : 뭔가 처리처리
               // text = text + text + text;
        //}

}