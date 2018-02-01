package com.noti.sns.utility;

public class DateChange {
    public static String intToStirng_Month(int p0) {
        String scroll_month = "";
        switch (p0) {
            case 0:
                scroll_month = "January";
                break;
            case 1:
                scroll_month = "February";
                break;
            case 2:
                scroll_month = "March";
                break;
            case 3:
                scroll_month = "April";
                break;
            case 4:
                scroll_month = "May";
                break;
            case 5:
                scroll_month = "June";
                break;
            case 6:
                scroll_month = "July";
                break;
            case 7:
                scroll_month = "August";
                break;
            case 8:
                scroll_month = "September";
                break;
            case 9:
                scroll_month = "October";
                break;
            case 10:
                scroll_month = "November";
                break;
            case 11:
                scroll_month = "December";
                break;
        }
        return scroll_month;
    }

    public static Boolean checkYunYear(int p0) {
        if (((p0 % 4 == 0) && !(p0 % 100 == 0)) || (p0 % 400 == 0)) {
            return true;
        }
        return false;
    }
}
