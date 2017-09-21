package com.five.high.emirim.testintent;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlusActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;

    private static final String TAG ="오캘:PlusActivity";

    private EditText mEventDateEditText;
    private EditText mEventTimeEditText;
    private EditText mEventNameEditText;
    private EditText mEventLocationEditText;
    private EditText mCopypasteEditText;


    // Firebase Realtime DB
    private DatabaseReference mDatabase;
    ClipboardManager mClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEventDateEditText = (EditText) findViewById(R.id.eventdate);
        mEventTimeEditText=(EditText)findViewById(R.id.eventtime);
        mEventNameEditText = (EditText) findViewById(R.id.eventname);
        mEventLocationEditText=(EditText) findViewById(R.id.eventlocation);
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mCopypasteEditText = (EditText) findViewById(R.id.copypaste);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mClipboard.hasPrimaryClip()) {
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            //Toast.makeText(this, item.getText(), Toast.LENGTH_SHORT).show();
            String paste = item.getText().toString();
            mCopypasteEditText.setText(paste);

            Pattern patternYear  =  Pattern.compile("(20[0-9][0-9])년");
            Matcher matchYear = patternYear.matcher(paste);
            String year;
            if(matchYear.find()){ // 이미지 태그를 찾았다면,,
                year = matchYear.group(1); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
             //mEventDateEditText.setText( match.group(1) + match.group(2) + match.group(3) );
            }
            Pattern patternMonth  =  Pattern.compile("([01]?[0-9])월");
            Matcher matchMonth = patternMonth.matcher(paste);
            String month;
            if(matchMonth.find()) { // 이미지 태그를 찾았다면,,
                month = matchMonth.group(1); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
            }
            Pattern patternDay  =  Pattern.compile("([0123]?[0-9])일");
            Matcher matchDay = patternDay.matcher(paste);
            String day;
            if(matchDay.find()) { // 이미지 태그를 찾았다면,,
                day = matchDay.group(1); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
            }
            mEventDateEditText.setText(matchYear.group(1) + matchMonth.group(1) + matchDay.group(1));
            Pattern patternTime  =  Pattern.compile("([01]?[0-9])시");
            Matcher matchTime = patternTime.matcher(paste);
            String time;
            if(matchTime.find()) { // 이미지 태그를 찾았다면,,
                time = matchTime.group(1); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
                if(time.length() == 1){
                    time = "0" + time;
                }
                mEventTimeEditText.setText(matchTime.group(1));
            }

            //장소 정규표현식으로 추출해야함...
            Pattern patternLocation  =  Pattern.compile("(\\w+)에서");
            Matcher matchLocation = patternLocation.matcher(paste);
            String location;
            if(matchLocation.find()) { // 이미지 태그를 찾았다면,,
                location = matchLocation.group(1); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
                mEventLocationEditText.setText(matchLocation.group(1));
            }
        }
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        Log.e(TAG, "onClick 실행");

        switch (view.getId()){
            case R.id.scheduleadd:
                try {


                    Log.e(TAG, "scheduleadd 눌렸네");
                    Boolean isError = false;
                    String errorMessage = "";

                    String date = mEventDateEditText.getText().toString().trim();
                    String time = mEventTimeEditText.getText().toString().trim();
                    String name = mEventNameEditText.getText().toString().trim();
                    String location = mEventLocationEditText.getText().toString().trim();

                    Log.e(TAG, "으악");
                    if (name == null || name == "") {
                        Toast.makeText(getApplicationContext(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }//if 괄호
                    else {
                        Toast.makeText(getApplicationContext(), "입력값" + date + " / " + time + " / " + name + " / " + location + " / ", Toast.LENGTH_SHORT).show();

                        String key = date + time;
                        Log.e(TAG, "키:" + key);
                        // 2017120811
                        CalEvent event = new CalEvent(name, location);
                        mDatabase.child("events").child(key).setValue(event);
                        Toast.makeText(getApplicationContext(), "추가되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }//else 괄호


                    Toast.makeText(this, "일정추가하기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e(TAG, "에러발생!!");
                }
                break;

        }
    }
}
