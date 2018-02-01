package com.noti.sns.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.noti.sns.R
import com.noti.sns.R.id.setting_logout
import com.noti.sns.activity.MainActivity.edit
import com.noti.sns.activity.MainActivity.api
import com.noti.sns.fragment.CalenderFragment
import com.noti.sns.schoolparsing.SchoolException
import com.noti.sns.schoolparsing.SchoolSchedule
import com.noti.sns.utility.DateChange.checkYunYear
import com.noti.sns.utility.Listsave
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*
import kotlin.collections.ArrayList


class SettingActivity : PreferenceActivity(), Preference.OnPreferenceClickListener {
    val today = Date()//오늘 날짜 객체
    var school_Schedule: ArrayList<List<SchoolSchedule>> = ArrayList()//학사일정 받아오기
    var check_down: Boolean = false
    var check_downfail: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        //기본선언
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting_activity)
        check_down = true
        val setting_activity_up: Preference = findPreference("setting_activity_up")
        setting_activity_up.setOnPreferenceClickListener(this)
        val setting_activity_help: Preference = findPreference("setting_activity_help")
        setting_activity_help.setOnPreferenceClickListener(this)
        val setting_activity_logout: Preference = findPreference("setting_activity_logout")
        setting_activity_logout.setOnPreferenceClickListener(this)

    }

    override fun onPreferenceClick(p0: Preference?): Boolean {
        check_down = true//초기상태는 다운로드 되어있음
        if (p0!!.getKey().equals("setting_activity_up")) {
            check_down = false//다운로드 시작
            var feb_Days: Int
            Toast.makeText(this, "학사일정을 불러옵니다.", Toast.LENGTH_SHORT).show()//토스트로 다운로드 알림
            //다운로드
            Thread {
                try {
                    for (i in 1..12) {
                        school_Schedule.add(api.getMonthlySchedule(today.year + 1900, i))
                    }

                    var c = 0
                    val m_31: List<Int> = listOf(3, 5, 7, 8, 10, 12)
                    val m_30: List<Int> = listOf(4, 6, 9, 11)
                    for (i in 3..12) {
                        if (m_31.contains(i))
                            for (j in 0..30) {
                                if (!school_Schedule[i - 1][j].schedule.equals("")) {
                                    edit.putInt("eventm" + c, i)
                                    edit.putInt("eventd" + c, j)
                                    c++
                                }
                            }
                        if (m_30.contains(i))
                            for (j in 0..29) {
                                if (!school_Schedule[i - 1][j].schedule.equals("")) {
                                    edit.putInt("eventm" + c, i)
                                    edit.putInt("eventd" + c, j)
                                    c++
                                }
                            }
                    }
                    for (j in 0..30) {
                        if (!school_Schedule[0][j].schedule.equals("")) {
                            edit.putInt("eventm" + c, 1)
                            edit.putInt("eventd" + c, j)
                            c++
                        }
                    }


                    if (checkYunYear(today.year + 1901))
                        feb_Days = 28
                    else
                        feb_Days = 27

                    for (j in 0..feb_Days) {
                        if (!school_Schedule[1][j].schedule.equals("")) {
                            edit.putInt("eventm" + c, 2)
                            edit.putInt("eventd" + c, j)
                            c++
                        }
                    }
                    edit.putInt("scnum", c)
                    edit.commit()
                    Listsave.SaveSchool.push_Hac(school_Schedule)

                    check_down = true
                } catch (e: SchoolException) {
                }
            }.start()
        } else if (p0.getKey().equals("setting_activity_help")) {
        } else if (p0.getKey().equals("setting_activity_logout")) {
            edit.putBoolean("save_login", false);
            edit.putBoolean("first", true);
            edit.commit()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        return false

    }

    //뒤로가기 설정
    override fun onBackPressed() {
        if (check_down) {
            super.onBackPressed()
            if (MenuActivity.where == 1)
                CalenderFragment.refresh()
        } else
        //다운로드 완료 전 뒤로가기 불가능
            Toast.makeText(this, "아직 불러오는 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
    }

}



