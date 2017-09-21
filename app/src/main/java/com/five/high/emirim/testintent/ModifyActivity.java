package com.five.high.emirim.testintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="오캘:PlusActivity";

    private EditText mEventDateEditText;
    private EditText mEventTimeEditText;
    private EditText mEventNameEditText;
    private EditText mEventLocationEditText;


    // Firebase Realtime DB
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEventDateEditText = (EditText) findViewById(R.id.eventdate);
        mEventTimeEditText=(EditText)findViewById(R.id.eventtime);
        mEventNameEditText = (EditText) findViewById(R.id.eventname);
        mEventLocationEditText=(EditText) findViewById(R.id.eventlocation);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        Log.e(TAG, "onClick 실행");




        switch (view.getId()){
            case R.id.scheduleadd:
                Log.e(TAG, "scheduleadd 눌렸네" );
                Boolean isError = false;
                String errorMessage = "";

                String date = mEventDateEditText.getText().toString().trim();
                String time = mEventTimeEditText.getText().toString().trim();
                String name = mEventNameEditText.getText().toString().trim();
                String location = mEventLocationEditText.getText().toString().trim();

//                Log.e(TAG, "날짜:" + date);
//                String rawPath = date.replaceAll("/", ".");
//
//                // date 는  . 빼서 처리
//                String[]  array = mEventDateEditText.getText().toString().trim().split("/");
//                for(String a : array){
//                    Log.e(TAG, "잘려진 내용:" + a );
//                }
                Log.e(TAG, "으악");
                if(name ==null || name ==""){
                    Toast.makeText(getApplicationContext(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                }//if 괄호
                else {
                    Toast.makeText(getApplicationContext(), "입력값" + date + " / " + time + " / " + name + " / " + location + " / ", Toast.LENGTH_SHORT).show();


//                    String year = "";
//                    String month = "";
//                    String day = "";
//
//                    year = array[0];
//                    month = array[1];
//                    day = array[2];
//
//                    // 2017.2.3   -> 20170203
//                    if(year.length()!=4){
//                        isError = true;
//                        errorMessage += "4자리 년도를 입력해 주세요.";
//                    }
//
//                    if(month.length()!=2){
//                        if(month.length() == 1){
//                            month = "0" + month;
//                        }else {
//                            isError = true;
//                            errorMessage += "올바른 월을 입력해주세요.";
//                        }
//                    }else {
//                        isError = true;
//                        errorMessage += "올바른 월을 입력해주세요.";
//                    }
//
//                    if(day.length()!=2){
//                        if(day.length() == 1){
//                           day = "0" + day;
//                        }else {
//                            isError = true;
//                            errorMessage += "올바른 일을 입력해주세요.";
//                        }
//                        isError = true;
//                        errorMessage += "올바른 일을 입력해주세요.";
//                    }
//
//                    try{
//                        int t = Integer.parseInt(time);
//
//                        if( t < 10){
//                            time = "0" + time;
//                        }
//
//                        if(t >= 0 && t <= 24){
//                            //
//                        }else{
//                            throw new Exception();
//                        }
//                    }catch(Exception e){
//                        isError = true;
//                        errorMessage += "올바른 일이 아닙니다.";
//                    }
//
//                    if(isError) {
//                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    String key = date  + time;
                    Log.e(TAG, "키:" + key);
                    // 2017120811
                    CalEvent event = new CalEvent(name, location);
                    mDatabase.child("events").child(key).setValue(event);


                }//else 괄호


                Toast.makeText(this,"일정추가하기 버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();

                break;
//            case R.id.autocomplete:
//                Log.e(TAG, "autocomplete 눌렸네" );
//                //name = mEventName.getText().toString();
//                Toast.makeText(this,"자동완성 버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();
//                // 2017091910
//                //CalEvent event = new CalEvent( name, location );
//                break;
        }
    }
}
