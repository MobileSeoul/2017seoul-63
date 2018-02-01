package com.noti.sns.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.noti.sns.R;

/**
 * Created by ngdb on 2017. 10. 31..
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HelpActivity extends Activity implements OnGestureListener {

    private LinearLayout main;
    private TextView viewA;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;
    static SharedPreferences pref;
    static SharedPreferences.Editor edit;
    static int positon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pref = this.getSharedPreferences("save", 0);
        edit = pref.edit();
        super.onCreate(savedInstanceState);
        gestureScanner = new GestureDetector(this);
        setContentView(R.layout.activity_help);
        positon = 0;
        change(positon);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gestureScanner.onTouchEvent(me);
    }

    public boolean onDown(MotionEvent e) {
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                positon++;
                if (positon > 4)
                    positon = 4;
                change(positon);
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                positon--;
                if (positon < 0)
                    positon = 0;
                change(positon);
            }
        } catch (Exception e) {

        }
        return true;
    }

    void change(int p0) {

        ImageView img = findViewById(R.id.help_image);
        ImageView dot[] = new ImageView[5];
        dot[0] = findViewById(R.id.dot_0);
        dot[1] = findViewById(R.id.dot_1);
        dot[2] = findViewById(R.id.dot_2);
        dot[3] = findViewById(R.id.dot_3);
        dot[4] = findViewById(R.id.dot_4);
        for (int i = 0; i < 5; i++)
            dot[i].setImageResource(R.drawable.non_action);
        dot[p0].setImageResource(R.drawable.btn_design);
        TextView tex = findViewById(R.id.help_info);
        switch (p0) {
            case 0:
                img.setImageResource(R.drawable.ic_supervisor_account_black_24dp);
                tex.setText("먼저 선생님께 방 이름을 받고\n회원가입 할 때 방 이름을 넣으세요");
                break;
            case 1:
                img.setImageResource(R.drawable.ic_network_wifi_black_24dp);
                tex.setText("SNS는 인터넷 연결이 필요합니다.");
                break;
            case 2:
                img.setImageResource(R.drawable.ic_timeline_black_24dp);
                tex.setText("홈에는 선생님들이 보낸 공지들이\n카드뷰로 보여집니다.");
                break;
            case 3:
                img.setImageResource(R.drawable.ic_access_alarm_black_24dp);
                tex.setText("급식을 원하는 시간에\n알림을 받으세요");
                break;
            case 4:
                img.setImageResource(R.drawable.ic_check_black_24dp);
                tex.setText("화면을 터치히여 앱을 시작하세요!");
                break;
        }
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    @Override
    public void onBackPressed() {
        if (!pref.getBoolean("see_tu", false))
            ActivityCompat.finishAffinity(this);
        else
            super.onBackPressed();
    }

    public boolean onSingleTapUp(MotionEvent e) {
        if (positon == 4) {
            edit.putBoolean("see_tu", true);
            edit.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }

}
