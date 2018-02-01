package com.noti.sns.utility;

import com.noti.sns.listitem.AlarmListItem;
import com.noti.sns.listitem.HomeCardListItem;
import com.noti.sns.schoolparsing.School;
import com.noti.sns.schoolparsing.SchoolMenu;
import com.noti.sns.schoolparsing.SchoolSchedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.noti.sns.activity.MainActivity.edit;
import static com.noti.sns.activity.MainActivity.pref;


public class Listsave {

    public static class MealAlamList {

        public static void add(String p0, int p1, int p2) {
            int add_num = pref.getInt("Alam_List_num", 0);
            remove(add_num);
            edit.putString("Alam_List_title" + add_num, p0);
            edit.putInt("Alam_List_hour" + add_num, p1);
            edit.putInt("Alam_List_minute" + add_num, p2);
            edit.putInt("Alam_List_num", add_num + 1);
            edit.commit();
        }

        public static void remove(int p0) {
            edit.remove("Alam_List_title" + p0);
            edit.remove("Alam_List_hour" + p0);
            edit.remove("Alam_List_minute" + p0);
            edit.commit();
        }

        public static void remove_all() {
            ArrayList<AlarmListItem> list = new ArrayList<>();
            put_Alam_List(list);
        }

        public static ArrayList<AlarmListItem> get_Alam_List() {
            ArrayList<AlarmListItem> retun = new ArrayList<>();
            int add_num = pref.getInt("Alam_List_num", 0);
            for (int i = 0; i < add_num; i++) {
                retun.add(new AlarmListItem(pref.getString("Alam_List_title" + i, ""), pref.getInt("Alam_List_hour" + i, 0), pref.getInt("Alam_List_minute" + i, 0)));
            }
            return retun;
        }

        public static void put_Alam_List(ArrayList<AlarmListItem> p0) {
            edit.putInt("Alam_List_num", p0.size());
            for (int i = 0; i < p0.size(); i++) {
                remove(i);
                edit.putString("Alam_List_title" + i, p0.get(i).wmeal);
                edit.putInt("Alam_List_hour" + i, p0.get(i).hour);
                edit.putInt("Alam_List_minute" + i, p0.get(i).minute);
            }
            edit.commit();
        }
    }

    public static class HomeCardList {

        public static void add(String p0, String p1, String p2) {
            int add_num = pref.getInt("Home_List_num", 0);
            remove(add_num);
            edit.putString("Home_List_title" + add_num, p0);
            edit.putString("Home_List_subtitle" + add_num, p1);
            edit.putString("Home_List_inside" + add_num, p2);
            edit.putInt("Home_List_num", add_num + 1);
            edit.commit();
        }

        public static void remove(int p0) {
            edit.remove("Home_List_title" + p0);
            edit.remove("Home_List_subtitle" + p0);
            edit.remove("Home_List_inside" + p0);
            edit.commit();
        }

        public static void remove_all() {
            ArrayList<HomeCardListItem> list = new ArrayList<>();
            put_Home_List(list);
        }

        public static ArrayList<HomeCardListItem> get_Home_List() {

            ArrayList<HomeCardListItem> retun = new ArrayList<>();
            ArrayList<HomeCardListItem> retun_r = new ArrayList<>();
            int add_num = pref.getInt("Home_List_num", 0);
            for (int i = 0; i < add_num; i++) {
                retun.add(new HomeCardListItem(pref.getString("Home_List_title" + i, ""), pref.getString("Home_List_subtitle" + i, ""), pref.getString("Home_List_inside" + i, "")));
                int dday = Dday.caldate(Integer.parseInt(retun.get(i).subtitle.split("\\.")[0]), Integer.parseInt(retun.get(i).subtitle.split("\\.")[1]), Integer.parseInt(retun.get(i).subtitle.split("\\.")[2]));
                if (dday <= 0)
                    retun_r.add(new HomeCardListItem(pref.getString("Home_List_title" + i, ""), pref.getString("Home_List_subtitle" + i, ""), pref.getString("Home_List_inside" + i, "")));


            }
            put_Home_List(retun_r);
            return retun_r;
        }

        public static void put_Home_List(ArrayList<HomeCardListItem> p0) {

            edit.putInt("Home_List_num", p0.size());
            for (int i = 0; i < p0.size(); i++) {
                remove(i);
                edit.putString("Home_List_title" + i, p0.get(i).title);
                edit.putString("Home_List_subtitle" + i, p0.get(i).subtitle);
                edit.putString("Home_List_inside" + i, p0.get(i).inside);
            }
            edit.commit();
        }
    }

    public static class SaveSchool {
        public static ArrayList<List<SchoolSchedule>> get_Hac() {

            ArrayList<List<SchoolSchedule>> rtn = new ArrayList<>();
            List line = new ArrayList();

            for (int j = 0; j < 31; j++) {
                line.add(new SchoolSchedule(pref.getString("event_s" + 0 + "/" + j, "")));
            }
            rtn.add(line);
            line = new ArrayList();

            int feb_Days = 28;
            if (DateChange.checkYunYear(new Date().getYear() + 1901))
                feb_Days = 29;
            for (int j = 0; j < feb_Days; j++) {
                line.add(new SchoolSchedule(pref.getString("event_s" + 1 + "/" + j, "")));
            }
            rtn.add(line);

            for (int i = 2; i < 12; i++) {
                line = new ArrayList();
                switch (i + 1) {
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        for (int j = 0; j < 31; j++) {
                            line.add(new SchoolSchedule(pref.getString("event_s" + i + "/" + j, "")));
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        for (int j = 0; j < 30; j++) {
                            line.add(new SchoolSchedule(pref.getString("event_s" + i + "/" + j, "")));
                        }
                        break;
                }
                rtn.add(line);
            }
            return rtn;
        }


        public static void push_Hac(ArrayList<List<SchoolSchedule>> p0) {

            for (int i = 2; i < 12; i++) {
                switch (i + 1) {
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        for (int j = 0; j < 31; j++) {
                            edit.putString("event_s" + i + "/" + j, p0.get(i).get(j).schedule);
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        for (int j = 0; j < 30; j++) {
                            edit.putString("event_s" + i + "/" + j, p0.get(i).get(j).schedule);
                        }
                        break;
                }
            }
            for (int j = 0; j < 31; j++) {
                edit.putString("event_s" + 0 + "/" + j, p0.get(0).get(j).schedule);
            }
            int feb_Days = 28;
            if (DateChange.checkYunYear(new Date().getYear() + 1901))
                feb_Days = 29;
            for (int j = 0; j < feb_Days; j++) {
                edit.putString("event_s" + 1 + "/" + j, p0.get(1).get(j).schedule);
            }
            edit.commit();
        }

        public static void put_meal_month(List<SchoolMenu> p0, int p1) {
            int days = 28;
            switch (p1) {
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
                    break;
            }
            if (DateChange.checkYunYear(new Date().getYear() + 1901))
                days = 29;
            for (int j = 0; j < days; j++) {
                edit.putString("meal_b" + p1 + "/" + j, p0.get(j).breakfast);
                edit.putString("meal_l" + p1 + "/" + j, p0.get(j).lunch);
                edit.putString("meal_d" + p1 + "/" + j, p0.get(j).dinner);
            }
            edit.commit();


        }

        public static List<SchoolMenu> get_meal_month(int p0) {
            int days = 28;
            switch (p0) {
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
                    break;
            }
            List<SchoolMenu> rt = new ArrayList();
            if (DateChange.checkYunYear(new Date().getYear() + 1901))
                days = 29;
            for (int j = 0; j < days; j++) {

                SchoolMenu rtn1 = new SchoolMenu();

                rtn1.breakfast = pref.getString("meal_b" + p0 + "/" + j, "조식이 없습니다");
                rtn1.lunch = pref.getString("meal_l" + p0 + "/" + j, "중식이 없습니다");
                rtn1.dinner = pref.getString("meal_d" + p0 + "/" + j, "석식이 없습니다");
                rt.add(rtn1);
            }
            return rt;


        }


    }


}


