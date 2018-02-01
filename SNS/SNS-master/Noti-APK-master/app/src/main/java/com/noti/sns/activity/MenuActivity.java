package com.noti.sns.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.noti.sns.fragment.CalenderFragment;
import com.noti.sns.fragment.HomeFragment;
import com.noti.sns.fragment.LunchFragment;
import com.noti.sns.R;

public class MenuActivity extends AppCompatActivity {

    public static boolean popup_on_alarm;//알람 설정용 팝업 관리
    BottomNavigationView bottomNavigationMenu;//바텀 네비게이션 객체
    TabHost tabHost1;//급식 프래그먼트의 탭 객체
    RelativeLayout popup;//급식 프래그먼트의 팝업 객체
    ImageView add_btn;//급식 프래그먼트의 추가버튼 객체
    public static int where;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //기본 선언
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationMenu = findViewById(R.id.bottom_navigation);//바텀 네비게이션 객체 초기화
        popup_on_alarm = false;//알람 설정용 팝업 초기화
        tabHost1 = findViewById(R.id.tabHost1);//탭 객체 초기화
        popup = findViewById(R.id.timepick_popup);//팝업 객체 초기화
        add_btn = findViewById(R.id.btnAlam);//추가버튼 객체 초기화

        switch_Fragment(0);//메뉴 액티비티 실행시 초기 프래그먼트 설정
        where = 0;
        //바텀 네비게이션의 아이템이 클릭됬을때
        bottomNavigationMenu.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    switch_Fragment(0);
                    break;
                case R.id.action_calender:
                    switch_Fragment(1);
                    break;
                case R.id.action_lunch:
                    switch_Fragment(2);
                    break;
            }
            return true;
        });
    }

    //프래그먼트를 바꾸는 함수
    void switch_Fragment(int p0) {
        //p0 : 선택된 프래그먼트 번호
        where = p0;
        //선택된 번호 아이템 이중클릭 방지 밑 애니메이션
        for (int i = 0; i < 3; i++) {
            if (i == p0)
                bottomNavigationMenu.getMenu().getItem(i).setEnabled(false);
            else
                bottomNavigationMenu.getMenu().getItem(i).setEnabled(true);
        }

        //선택된 번호 아이템으로 프래그먼트 이동
        switch (p0) {
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_menu, new HomeFragment())
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_menu, new CalenderFragment())
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_menu, new LunchFragment())
                        .commit();
                break;
        }
    }

    //뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        if (popup_on_alarm) {
            //알람 팝업이 있을때
            bottomNavigationMenu = findViewById(R.id.bottom_navigation);//바텀 네비게이션 객체 초기화
            popup_on_alarm = false;//알람 설정용 팝업 초기화
            tabHost1 = findViewById(R.id.tabHost1);//탭 객체 초기화
            popup = findViewById(R.id.timepick_popup);//팝업 객체 초기화
            add_btn = findViewById(R.id.btnAlam);//추가버튼 객체 초기화
            popup.setVisibility(View.GONE);
            popup_on_alarm = false;
            add_btn.setEnabled(true);
            tabHost1.getTabWidget().getChildTabViewAt(0).setEnabled(true);
            tabHost1.getTabWidget().getChildTabViewAt(1).setEnabled(true);
            tabHost1.getTabWidget().getChildTabViewAt(2).setEnabled(true);
        } else
            //팝업이 없을때
            ActivityCompat.finishAffinity(this);
    }
}
