package com.noti.sns.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.noti.sns.activity.MainActivity;
import com.noti.sns.activity.SettingActivity;
import com.noti.sns.listitem.AlarmListItem;
import com.noti.sns.R;
import com.noti.sns.schoolparsing.SchoolMenu;
import com.noti.sns.server.Connection;
import com.noti.sns.utility.BtnPress;
import com.noti.sns.utility.Listsave;
import com.noti.sns.viewadapter.AlarmViewAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.noti.sns.activity.MainActivity.pref;
import static com.noti.sns.activity.MenuActivity.popup_on_alarm;

public class LunchFragment extends Fragment {

    public static ArrayList<AlarmListItem> contacts;
    public static Context context;
    static RecyclerView recyclerView;
    public static View rootView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frg_menu_lunch, container, false);
        TabHost tabHost1 = rootView.findViewById(R.id.tabHost1);
        RelativeLayout popup = rootView.findViewById(R.id.timepick_popup);
        ImageView add_btn = rootView.findViewById(R.id.btnAlam);
        TextView cancel = rootView.findViewById(R.id.timepick_cancel);
        TextView ok = rootView.findViewById(R.id.timepick_ok);
        RadioGroup radioGroup = rootView.findViewById(R.id.select_meal_time);
        TimePicker timePicker = rootView.findViewById(R.id.timepicker);
        Date today = new Date();
        popup_on_alarm = false;
        context = getActivity();

        popup.setVisibility(View.VISIBLE);
        popup.setVisibility(View.GONE);
        List<SchoolMenu> ex = Listsave.SaveSchool.get_meal_month(today.getMonth() + 1);

        TextView c1_d = rootView.findViewById(R.id.c1_d);
        TextView c1_i = rootView.findViewById(R.id.c1_i);
        TextView c2_d = rootView.findViewById(R.id.c2_d);
        TextView c2_i = rootView.findViewById(R.id.c2_i);
        TextView c3_d = rootView.findViewById(R.id.c3_d);
        TextView c3_i = rootView.findViewById(R.id.c3_i);

        c1_d.setText(today.getMonth() + 1 + "월 " + today.getDate() + "일");
        c2_d.setText(today.getMonth() + 1 + "월 " + today.getDate() + "일");
        c3_d.setText(today.getMonth() + 1 + "월 " + today.getDate() + "일");
        c1_i.setText(ex.get(today.getDate() - 1).breakfast);
        c2_i.setText(ex.get(today.getDate() - 1).lunch);
        c3_i.setText(ex.get(today.getDate() - 1).dinner);


        tabHost1.setup();
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("조식");
        tabHost1.addTab(ts1);
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("중식");
        tabHost1.addTab(ts2);
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("석식");
        tabHost1.addTab(ts3);
        tabHost1.setCurrentTab(1);


        recyclerView = rootView.findViewById(R.id.recyclerView_m);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();

        final String[] mealw = {""};


        add_btn.setOnClickListener(view -> {
            if (!popup_on_alarm) {
                popup_on_alarm = true;
                popup.setVisibility(View.VISIBLE);
                add_btn.setEnabled(false);
                tabHost1.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                tabHost1.getTabWidget().getChildTabViewAt(1).setEnabled(false);
                tabHost1.getTabWidget().getChildTabViewAt(2).setEnabled(false);
                radioGroup.check(R.id.rad2);
            }
        });
        add_btn.setOnTouchListener((view, motionEvent) -> BtnPress.addBTN(motionEvent, add_btn));

        ok.setOnClickListener((View view) -> {
            if (popup_on_alarm) {
                mealw[0] = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rad1:

                            mealw[0] = "조식";
                            break;
                        case R.id.rad2:

                            mealw[0] = "중식";
                            break;
                        case R.id.rad3:

                            mealw[0] = "석식";
                            break;
                        default:
                    }
                    if (Alarm_isNaN(radioGroup.getCheckedRadioButtonId())) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("pw", pref.getString("save_pw", ""));
                            jo.put("type", mealw[0]);
                            jo.put("hour", timePicker.getHour());
                            jo.put("min", timePicker.getMinute());
                            jo.put("id", pref.getString("save_id", ""));
                            String response[] = Connection.sendJSON(getString(R.string.url) + "/alarm/", jo.toString());
                            if (response[1].equals("alarm_success")) {
                                Listsave.MealAlamList.add(mealw[0], timePicker.getHour(), timePicker.getMinute());
                            } else {
                                Toast.makeText(getActivity(), "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        refresh();
                    } else
                        Toast.makeText(getActivity(), "이미 " + mealw[0] + " 알람이 있습니다.", Toast.LENGTH_SHORT).show();

                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rad1:
                            mealw[0] = "조식";
                            break;
                        case R.id.rad2:
                            mealw[0] = "중식";
                            break;
                        case R.id.rad3:
                            mealw[0] = "석식";

                            break;
                        default:
                    }
                    if (Alarm_isNaN(radioGroup.getCheckedRadioButtonId())) {
                        JSONObject jo = new JSONObject();
                        /*
                            alarm_failed:nonexistent //계정없음
							alarm_failed:password
							alarm_success
			             */
                        try {
                            jo.put("pw", pref.getString("save_pw", ""));
                            jo.put("type", mealw[0]);
                            jo.put("hour", timePicker.getCurrentHour());
                            jo.put("min", timePicker.getCurrentMinute());
                            jo.put("id", pref.getString("save_id", ""));
                            String[] response = Connection.sendJSON(getString(R.string.url) + "/alarm/", jo.toString());
                            if (response[1].equals("alarm_success")) {
                                Listsave.MealAlamList.add(mealw[0], timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                            } else {
                                Toast.makeText(getActivity(), "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        refresh();
                    } else
                        Toast.makeText(getActivity(), "이미 " + mealw[0] + " 알람이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                popup.setVisibility(View.GONE);
                popup_on_alarm = false;
                add_btn.setEnabled(true);
                if (Listsave.MealAlamList.get_Alam_List().size() == 3)
                    add_btn.setVisibility(View.GONE);
                tabHost1.getTabWidget().getChildTabViewAt(0).setEnabled(true);
                tabHost1.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                tabHost1.getTabWidget().getChildTabViewAt(2).setEnabled(true);
            }

        });


        cancel.setOnClickListener(view -> {
            if (popup_on_alarm) {
                popup.setVisibility(View.GONE);
                popup_on_alarm = false;
                add_btn.setEnabled(true);
                tabHost1.getTabWidget().getChildTabViewAt(0).setEnabled(true);
                tabHost1.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                tabHost1.getTabWidget().getChildTabViewAt(2).setEnabled(true);
            }

        });

        final RadioGroup rg = rootView.findViewById(R.id.select_meal_time);

        int id = rg.getCheckedRadioButtonId();
        RadioButton rb = rootView.findViewById(id);
        ImageView goToSetting_Lunch = rootView.findViewById(R.id.goToSetting_Lunch);
        Intent intent_settitng = new Intent(getActivity(), SettingActivity.class);
        goToSetting_Lunch.setOnClickListener(view -> startActivity(intent_settitng));

        return rootView;
    }

    public static void refresh() {
        ImageView add_btn = rootView.findViewById(R.id.btnAlam);
        AlarmViewAdapter adapter;
        ArrayList<AlarmListItem> contacts;
        contacts = Listsave.MealAlamList.get_Alam_List();
        if (contacts.size() == 3)
            add_btn.setVisibility(View.GONE);
        else
            add_btn.setVisibility(View.VISIBLE);
        adapter = new AlarmViewAdapter(context, contacts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        contacts = Listsave.MealAlamList.get_Alam_List();
        Listsave.MealAlamList.put_Alam_List(contacts);
        super.onPause();
    }

    public static boolean Alarm_isNaN(int p0) {
        ArrayList<AlarmListItem> alarmListItems = Listsave.MealAlamList.get_Alam_List();
        String meal = null;
        if (p0 == R.id.rad1)
            meal = "조식";

        if (p0 == R.id.rad2)
            meal = "중식";

        if (p0 == R.id.rad3)
            meal = "석식";
        for (int i = 0; i < alarmListItems.size(); i++) {
            if (alarmListItems.get(i).wmeal.equals(meal))
                return false;
        }
        return true;
    }


}
