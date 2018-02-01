package com.noti.sns.listitem;

public class AlarmListItem {
    public String wmeal;
    public int hour;
    public int minute;

    // 화면에 표시될 문자열 초기화
    public AlarmListItem(String p0, int p1, int p2) {
        this.wmeal = p0;
        this.hour = p1;
        this.minute = p2;
    }
}
