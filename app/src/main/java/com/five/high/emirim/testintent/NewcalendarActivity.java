package com.five.high.emirim.testintent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class NewcalendarActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener {

    private static final String TAG = "캘:NewcalAct";
    final String[] LIST_MENU = {"일정추가하기", "공유하기"} ;

    private MaterialCalendarView mWidget;
    private TextView mTextView;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcalendar);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ListView listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(adapter) ;


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                // TODO : use strText
            }
        }) ;

        mWidget = (MaterialCalendarView) findViewById(R.id.calendarView);
        mTextView = (TextView) findViewById(R.id.textView);

        mWidget.setOnDateChangedListener(this);
        //
        mWidget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);


        Calendar instance = Calendar.getInstance();
        mWidget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        mWidget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        mWidget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

    }

    //// TODO: 2017-09-18 이거 왜 에러 떴는지 수정하기 각 화면으로 넘겨지는 것~!
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view,int i, long j){
//      String c_list=LIST_MENU[Integer.parseInt("일정추가하기")];
//        Intent intent = new Intent(NewcalendarActivity.this, PlusActivity.class);
//        intent.putExtra("arr_text",c_list);
//        new PlusActivity(intent);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view,int i, long j){
//        String c_list=LIST_MENU[Integer.parseInt("공유하기")];
//        Intent intent = new Intent(NewcalendarActivity.this,ShareActivity.class);
//        intent.putExtra("arr_text",c_list);
//        new PlusActivity(intent);
//    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

    }

    public ArrayList<CalendarDay> getFBEvents() {
        ArrayList<CalendarDay> events = new ArrayList<>();
        Log.e(TAG, "getFB이벤트 호출~~");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "파이어베이스 값 변경 이벤트 동작");
                // Get Post object and use the values to update the UI
                CalEvent event = dataSnapshot.getValue(CalEvent.class);
                Log.e(TAG, "으아아" + event.name + "/" + event.location);
                //events.add();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("events").addValueEventListener(postListener);

        return events;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.shareTempButton:
                try {
                    Uri uri = Uri.parse("smsto:"); //sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 SMS문자보내기 Activity의 action값
                    i.putExtra("sms_body", "Hello...");  //보낼 문자내용을 추가로 전송, key값은 반드시 'sms_body'
                    startActivity(i);//액티비티 실행
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "현재 기기에는 문자 보내는 기능이 없습니다. ^^;",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();

            //
            //calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            Log.e(TAG, "getFB이벤트??");
            dates = getFBEvents();

            calendar.set(2017, 9 - 1, 15);
            dates.add(CalendarDay.from(calendar));

//            for (int i = 0; i < 30; i++) {
//                CalendarDay day = CalendarDay.from(calendar);
//                dates.add(day);
//                calendar.add(Calendar.DATE, 5);
//            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            mWidget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }

}
