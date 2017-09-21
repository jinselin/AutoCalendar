package com.five.high.emirim.testintent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.five.high.emirim.testintent.decorators.EventDecorator;
import com.five.high.emirim.testintent.decorators.HighlightWeekendsDecorator;
import com.five.high.emirim.testintent.decorators.MySelectorDecorator;
import com.five.high.emirim.testintent.decorators.OneDayDecorator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewcalendarActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener {

    private static final String TAG = "캘:NewcalAct";

    // 테스트용 리스트 내용물
    final String[] LIST_MENU = {"일정추가하기", "공유하기"} ;
    ArrayList<CalEventWithKey> mEventWithKeys = new ArrayList<>();
    ArrayList<String> mEventStrings = new ArrayList<>();
    ArrayList<CalEventWithKey> mEventSelectedDay = new ArrayList<>();
    ArrayList<CalendarDay> mEvents;

    // 마테리얼 칼렌더
    private MaterialCalendarView mWidget;
    // 마테리얼 칼렌더 장식용
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    // 마테리얼 칼랜더 밑에 출력되는 메시지
    private TextView mTextView;

    // 마테리얼 칼랜더 밑에 있는 일정 목록
    private ListView mListview;
    private ArrayAdapter mAdapter;
    // Firebase Realtime DB
    private DatabaseReference mDatabase;
    private CalendarDay mLastDate;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_newcalendar);

        setContentView(R.layout.activity_newcalendar);

        Button goplusbtn=(Button)findViewById(R.id.goplustbtn);


        goplusbtn.setOnClickListener(
                new Button.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(NewcalendarActivity.this, PlusActivity.class);
                        startActivity(intent);
                    }
                }
        );

        // Firebase DB 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // 리스트에서 보여줄 어댑터 생성
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mEventStrings);

        // 리스트
        mListview = (ListView) findViewById(R.id.listview);
        // 리스트에 어댑터 삽입
        mListview.setAdapter(mAdapter);

        // 리스트 안에 아이템 클릭시 동작 - SNS 공유하기
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                try {
                    Uri uri = Uri.parse("smsto:");
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    CalEventWithKey c = mEventSelectedDay.get(position);
                    String key = c.key;
                    String year = key.substring(0, 4);
                    String mon = key.substring(4, 6);
                    String day = key.substring(6, 8);
                    String hour = key.substring(8, 10);
                    String msg = year + "년 " + mon + "월 " + day + "일 " + hour + "시 " + c.location + "에서 " + c.name + "!!!";
                    intent.putExtra("sms_body", msg);  //보낼 문자내용을 추가로 전송, key값은 반드시 'sms_body'
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "문자를 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 리스트 안에 아이템 클릭시 동작 - 일정 수정/삭제하기 액티비티로 넘어가기
        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                Intent intent= new Intent(NewcalendarActivity.this, ModifyActivity.class);
//                                intent.putExtra("key", mEventSelectedDay.get(position).key);
//                                startActivity(intent);
                mDatabase.child("events/" + mEventSelectedDay.get(position).key).removeValue();
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 마테리얼 칼렌더 레퍼런스 받아오기
        mWidget = (MaterialCalendarView) findViewById(R.id.calendarView);
        //mTextView = (TextView) findViewById(R.id.textView);

        // 날짜 클릭 감지, 처리하는 리스너
        mWidget.setOnDateChangedListener(this);

        // 전/후 월의 추가 날짜 보여주기
        mWidget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // 오늘 날짜 받아오기
        Calendar instance = Calendar.getInstance();
        // 캘린더에 오늘 날짜 선택
        mWidget.setSelectedDate(instance.getTime());

        // 캘린더 시작 년도, 월, 일 지정. 기본적으로 올해의 1월 1일
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        // 캘린더의 끝 년도, 월, 일 지정. 기본적으로 올해의 12월 31일
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        // 캘린더 시작, 끝 날짜 지정
        mWidget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        // 캘린더 장식하기]
        mWidget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        // 캘린더에 일정을 읽어와서 표시해주는 코드!!(백그라운드로 동작)
        //new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        testdo();

    }

    private void testdo() {

        // 일정들 목록
        mEvents = new ArrayList<>();
        Log.e(TAG, "파이어베이스에서 일정 가져오기 호출");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                try{
                mEventWithKeys.clear();
                mEventSelectedDay.clear();
                for (DataSnapshot calEventSnapshot: dataSnapshot.getChildren()) {
                    String key = calEventSnapshot.getKey();
                    try {
                        int year = Integer.parseInt(key.substring(0, 4));
                        int mon = Integer.parseInt(key.substring(4, 6));
                        int day = Integer.parseInt(key.substring(6, 8));
                        int hour = Integer.parseInt(key.substring(8, 10));

                        Log.e(TAG, "가져온 키값들 : " + calEventSnapshot.getKey());
                        CalEvent event = calEventSnapshot.getValue(CalEvent.class);
                        Log.e(TAG, "가져온 일정들 : " + event.toString() );

                        Calendar calendar = Calendar.getInstance();
                        Log.e(TAG, "" + year + "." + mon + "." + day );
                        calendar.set(year,      // 년도
                                mon - 1,   // 월 (1 빼야함) kj
                                day);      // 일
                        mEvents.add(CalendarDay.from(calendar));
                        mEventWithKeys.add(new CalEventWithKey( event.name, event.location, key));
                    }catch (Exception e){
                        continue;
                    }

                }

                mWidget.addDecorator(new EventDecorator(Color.RED, mEvents));
                if(mLastDate != null) {
                    renweTodayList(null);
                }else{
                    Calendar instance = Calendar.getInstance();
                    renweTodayList( CalendarDay.from(instance));
                }

                //Log.e(TAG, "으아아" + event.name + "/" + event.location);

//                }catch (Exception e){
//
//                    Toast.makeText(getApplicationContext(), "파이어베이서 에러! 인터넷 에러?", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("events").addValueEventListener(postListener);

    }

    // 마테리얼 캘린더에서 날짜 선택시 호출되는 코드
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        mLastDate = date;
        oneDayDecorator.setDate(date.getDate());
        renweTodayList(date);
        widget.invalidateDecorators();

    }

    public void renweTodayList( CalendarDay date){
        if(date == null){
            date = mLastDate;
        }

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
        String today = mSimpleDateFormat.format ( date.getDate() );
        Log.e(TAG, "오늘은 : " + today);
        mEventStrings.clear();
        for( CalEventWithKey c : mEventWithKeys){
            String d = c.key.substring(0, 8);
            Log.e(TAG, "비교날짜" + d);
            String t = c.key.substring(8,10);
            Log.e(TAG, "시간" + t);
            if( today.equals(d) ){
                mEventStrings.add(  t + "시 : " + c.name + " in " + c.location );
                mEventSelectedDay.add(c);
            }
        }
        mAdapter.notifyDataSetInvalidated();
    }

    // 뭔가 클릭 시 처리하는 메소드
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.shareTempButton:
                try {
                    Uri uri = Uri.parse("smsto:"); //sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 SMS문자보내기 Activity의 action값
                    i.putExtra("sms_body", " ");  //보낼 문자내용을 추가로 전송, key값은 반드시 'sms_body'
                    startActivity(i);//액티비티 실행
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "현재 기기에는 문자 보내는 기능이 없습니다. ^^;",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
