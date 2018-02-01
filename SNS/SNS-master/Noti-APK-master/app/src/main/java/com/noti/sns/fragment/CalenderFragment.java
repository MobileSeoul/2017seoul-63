package com.noti.sns.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.noti.sns.activity.SettingActivity;
import com.noti.sns.listitem.TimeLineListItem;
import com.noti.sns.R;
import com.noti.sns.schoolparsing.SchoolSchedule;
import com.noti.sns.utility.DateChange;
import com.noti.sns.utility.Listsave;
import com.noti.sns.viewadapter.TimeLineViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.noti.sns.activity.MainActivity.pref;

public class CalenderFragment extends Fragment {

    static ArrayList<List<SchoolSchedule>> schedule_ForCalender;//학사일정 리스트
    static ArrayList<TimeLineListItem> contacts_t;//타임라인 리스트
    static RecyclerView recyclerView_t;//리사이클러 뷰 객체
    static TimeLineViewAdapter adapter_t;//타임라인 어댑터 객체
    static CompactCalendarView compactCalendarView;//캘린더뷰
    static Context context;
    static View rootView;//루트뷰
    TextView cal_month;//월 텍스트뷰

    //기본선언
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Date today = new Date();//오늘 날짜
        context = getActivity();
        rootView = inflater.inflate(R.layout.frg_menu_calender, container, false);//루트뷰 초기화
        cal_month = rootView.findViewById(R.id.cal_month);//월 텍스트뷰 초기화
        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);//캘린더뷰 초기화
        recyclerView_t = rootView.findViewById(R.id.recyclerView_t);//리사이클러 뷰 초기화
        contacts_t = new ArrayList<>();//타임라인 리스트 초기화
        Intent intent_settitng = new Intent(getActivity(), SettingActivity.class);//새팅 인텐트
        ImageView goToSetting_Home = rootView.findViewById(R.id.goToSetting_Calender);//세팅 버튼 이미지뷰
        schedule_ForCalender = Listsave.SaveSchool.get_Hac();//학사일정 초기화
        if (schedule_ForCalender.size() != 0)
            //0아닐때만 추가 실행
            contacts_t = make_contact(today.getYear(), today.getMonth(), today.getDate());
        adapter_t = new TimeLineViewAdapter(getActivity(), contacts_t);//타임라인 어댑터 초기화

        cal_month.setText(DateChange.intToStirng_Month(today.getMonth()));//캘린더 월 표시

        //어댑터 기본 초기화
        recyclerView_t.setHasFixedSize(true);
        recyclerView_t.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_t.setAdapter(adapter_t);

        compactCalendarView.setUseThreeLetterAbbreviation(true);//3글자 월 알려주기
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);//일주일 일요일부터 시작
        //캘린더뷰 리스너
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            //날짜 클릭될때
            @Override
            public void onDayClick(Date dateClicked) {
                contacts_t = make_contact(dateClicked.getYear(), dateClicked.getMonth(), dateClicked.getDate());
                adapter_t = new TimeLineViewAdapter(getActivity(), contacts_t);
                recyclerView_t.setAdapter(adapter_t);
            }

            //스와이프 할때
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //월 텍스트 바뀜
                cal_month.setText(DateChange.intToStirng_Month(firstDayOfNewMonth.getMonth()));

            }
        });
        //이벤트 밀리세컨드로 지정
        for (int i = 0; i < pref.getInt("scnum", 0) + 1; i++) {
            try {
                long long1 = 0;
                if (pref.getInt("eventm" + i, 0) >= 3)
                    long1 = Long.parseLong(String.valueOf(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse((pref.getInt("eventm" + i, 0)) + "/" + (pref.getInt("eventd" + i, 0) + 1) + "/2017 01:00:00").getTime()), 10);
                else
                    long1 = Long.parseLong(String.valueOf(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse((pref.getInt("eventm" + i, 0)) + "/" + (pref.getInt("eventd" + i, 0) + 1) + "/2018 01:00:00").getTime()), 10);

                compactCalendarView.addEvent(new Event(Color.parseColor("#36afff"), long1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //세팅 프래그먼트
        goToSetting_Home.setOnClickListener(view -> startActivity(intent_settitng));


        return rootView;
    }


    public static void refresh() {
        contacts_t = new ArrayList<>();//타임라인 리스트 초기화
        schedule_ForCalender = Listsave.SaveSchool.get_Hac();//학사일정 초기화
        Date today = new Date();
        int c_year = today.getMonth() == 0 || today.getMonth() == 1 ? today.getYear() - 1 : today.getYear();
        if (schedule_ForCalender.size() != 0)
            //0아닐때만 추가 실행
            contacts_t = make_contact(c_year, new Date().getMonth(), new Date().getDate());
        adapter_t = new TimeLineViewAdapter(context, contacts_t);//타임라인 어댑터 초기화
        compactCalendarView.removeAllEvents();
        for (int i = 0; i < pref.getInt("scnum", 0) + 1; i++) {
            try {
                long long1 = 0;
                if (pref.getInt("eventm" + i, 0) >= 3)
                    long1 = Long.parseLong(String.valueOf(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse((pref.getInt("eventm" + i, 0)) + "/" + (pref.getInt("eventd" + i, 0) + 1) + "/2017 01:00:00").getTime()), 10);
                else
                    long1 = Long.parseLong(String.valueOf(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse((pref.getInt("eventm" + i, 0)) + "/" + (pref.getInt("eventd" + i, 0) + 1) + "/2018 01:00:00").getTime()), 10);

                compactCalendarView.addEvent(new Event(Color.parseColor("#36afff"), long1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        recyclerView_t.setAdapter(adapter_t);
    }

    //이벤트 모두 생성
    static ArrayList<TimeLineListItem> make_contact(int year, int month, int dayOfMonth) {
        ArrayList<TimeLineListItem> contacts_t = new ArrayList<>();
        Date today = new Date();
        int c_year = today.getMonth() == 0 || today.getMonth() == 1 ? today.getYear() - 1 : today.getYear();
        if (schedule_ForCalender.get(month).get(dayOfMonth - 1).schedule.equals("") || ((month >= 2 && year != c_year) || (month < 2 && year != c_year + 1)))
            contacts_t.add(new TimeLineListItem(year + 1900 + "년 " + (month + 1) + "월 " + dayOfMonth + "일", "이 날은 학사일정이 없습니다."));
        if (year < c_year) {
            year = c_year;
            month = 0;
            dayOfMonth = 1;
        }
        for (int i = 0; i < pref.getInt("scnum", 0) + 1; i++) {
            if (c_year == year) {
                if (pref.getInt("eventm" + i, 0) > 2 && (pref.getInt("eventm" + i, 0) > month + 1 || (pref.getInt("eventm" + i, 0) == month + 1 && pref.getInt("eventd" + i, 0) >= dayOfMonth - 1))) {
                    contacts_t.add(new TimeLineListItem(year + 1900 + "년 " + pref.getInt("eventm" + i, 0) + "월 " + (pref.getInt("eventd" + i, 0) + 1) + "일", schedule_ForCalender.get(pref.getInt("eventm" + i, 0) - 1).get(pref.getInt("eventd" + i, 0)).schedule));
                } else if (pref.getInt("eventm" + i, 0) != 0 && pref.getInt("eventm" + i, 0) <= 2) {
                    contacts_t.add(new TimeLineListItem(year + 1901 + "년 " + pref.getInt("eventm" + i, 0) + "월 " + (pref.getInt("eventd" + i, 0) + 1) + "일", schedule_ForCalender.get(pref.getInt("eventm" + i, 0) - 1).get(pref.getInt("eventd" + i, 0)).schedule));
                }
            } else if (c_year + 1 == year) {
                if (pref.getInt("eventm" + i, 0) <= 2 && (pref.getInt("eventm" + i, 0) > month + 1 || (pref.getInt("eventm" + i, 0) == month + 1 && pref.getInt("eventd" + i, 0) >= dayOfMonth - 1))) {
                    contacts_t.add(new TimeLineListItem(year + 1900 + "년 " + pref.getInt("eventm" + i, 0) + "월 " + (pref.getInt("eventd" + i, 0) + 1) + "일", schedule_ForCalender.get(pref.getInt("eventm" + i, 0) - 1).get(pref.getInt("eventd" + i, 0)).schedule));
                }
            }

        }

        return contacts_t;
    }


}
