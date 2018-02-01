package com.noti.sns.utility;

import android.app.Activity;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.noti.sns.R;

public class BtnPress extends Activity {
    public static Boolean bigBTN(MotionEvent e, ImageView btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btn.setBackgroundResource(R.drawable.btn_design_pressed);
                    btn.setElevation(0F);
                    switch (btn.getId()) {
                        case R.id.login_btn:
                            btn.setImageResource(R.drawable.ic_arrow_white_24dp);
                            break;
                        case R.id.register_confirm:
                            btn.setImageResource(R.drawable.ic_check_black_24dp);
                            break;
                        case R.id.btnAlam:
                            btn.setImageResource(R.drawable.ic_check_black_24dp);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    btn.setBackgroundResource(R.drawable.btn_design);
                    btn.setElevation(5F);
                    switch (btn.getId()) {
                        case R.id.login_btn:
                            btn.setImageResource(R.drawable.ic_arrow_forward_24dp);
                            break;
                        case R.id.register_confirm:
                            btn.setImageResource(R.drawable.ic_check_blue_24dp);
                            break;
                        case R.id.btnAlam:
                            btn.setImageResource(R.drawable.ic_check_black_24dp);
                            break;
                    }


            }
        }
        return false;
    }

    public static Boolean addBTN(MotionEvent e, ImageView btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btn.setBackgroundResource(R.drawable.btn_design);
                    btn.setElevation(0F);
                    switch (btn.getId()) {
                        case R.id.btnAlam:
                            btn.setImageResource(R.drawable.ic_add_black_24dp);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    btn.setBackgroundResource(R.drawable.btn_design_pressed);
                    btn.setElevation(5F);
                    switch (btn.getId()) {

                        case R.id.btnAlam:
                            btn.setImageResource(R.drawable.ic_add_white);
                            break;
                    }


            }
        }
        return false;
    }

    public static Boolean smallBTN(MotionEvent e, ImageView btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btn.setBackgroundResource(R.drawable.btn_design2_pressed);
                    switch (btn.getId()) {
                        case R.id.register_btn:
                            btn.setImageResource(R.drawable.ic_person_add_blue_24dp);
                            break;
                        case R.id.passwd_btn:
                            btn.setImageResource(R.drawable.ic_help_black_24dp);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    btn.setBackgroundResource(R.drawable.btn_design2);
                    switch (btn.getId()) {
                        case R.id.register_btn:
                            btn.setImageResource(R.drawable.ic_person_add_black_24dp);
                            break;
                        case R.id.passwd_btn:
                            btn.setImageResource(R.drawable.ic_help_white_24dp);
                            break;
                    }


            }
        }
        return false;
    }

    public static Boolean pinBTN(MotionEvent e, TextView btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btn.setBackgroundResource(R.drawable.btn_pin_pressed);
                    break;
                case MotionEvent.ACTION_UP:
                    btn.setBackgroundResource(R.drawable.btn_pin);
                    break;
            }
        }
        return false;
    }

}