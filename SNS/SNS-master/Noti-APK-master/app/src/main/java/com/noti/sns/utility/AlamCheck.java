package com.noti.sns.utility;

import com.noti.sns.listitem.AlarmListItem;

import java.util.ArrayList;

public class AlamCheck {

    public static boolean Alarm_isNaN(int p0) {
        ArrayList<AlarmListItem> alarmListItems = Listsave.MealAlamList.get_Alam_List();
        String meal = null;
        if (p0 == 0)
            meal = "조식";

        if (p0 == 1)
            meal = "중식";

        if (p0 == 2)
            meal = "석식";
        for (int i = 0; i < alarmListItems.size(); i++) {
            if (alarmListItems.get(i).wmeal.equals(meal))
                return false;
        }
        return true;
    }
}
