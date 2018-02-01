package com.noti.sns.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.Preference;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.noti.sns.R;
import com.noti.sns.activity.MainActivity;
import com.noti.sns.schoolparsing.School;
import com.noti.sns.schoolparsing.SchoolException;
import com.noti.sns.schoolparsing.SchoolMenu;
import com.noti.sns.utility.Listsave;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Date today = new Date();
        Map<String, String> data = remoteMessage.getData();

        SharedPreferences pref = this.getSharedPreferences("save", 0);
        SharedPreferences.Editor edit = pref.edit();
        final boolean[] new_month = {false};
        if (data.size() > 0) {
            if (data.containsKey("type")) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder builder;
                String meal[] = new String[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("my_notification_channel", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.setDescription("Channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationManager.createNotificationChannel(notificationChannel);
                    builder = new Notification.Builder(this, "my_notification_channel");
                } else {
                    builder = new Notification.Builder(this);
                }


                if ((pref.getInt("mealMonth", 0) != today.getMonth() + 1)) {
                    new_month[0] = true;
                    //다운로드 안되있음
                    //파싱을 활용한 다운로드
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                School api = new School(School.Type.HIGH, School.Region.SEOUL, pref.getString("school_use", ""));

                                if (pref.getString("school_use", "").equals("")) {

                                } else {
                                    Listsave.SaveSchool.put_meal_month(api.getMonthlyMenu(today.getYear() + 1900, today.getMonth() + 1), today.getMonth() + 1);//교육청 파싱하여 급식 불러옴
                                    new_month[0] = false;
                                    //다운받아진 달 저장
                                    edit.putInt("mealMonth", today.getMonth() + 1);
                                    edit.commit();
                                }
                                //다운로드 확인
                            } catch (SchoolException e) {
                                //다운로드 실패
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }


                builder.setSmallIcon(R.drawable.bellxxxhdpi);
                builder.setContentText("식단");
                builder.setWhen(System.currentTimeMillis());
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                List<SchoolMenu> ex = Listsave.SaveSchool.get_meal_month(today.getMonth() + 1);
                if (data.get("type").equals("조식")) { //아침 알람

                    builder.setContentTitle("조식 알림");
                    if (new_month[0])
                        meal = new String[]{"급식 다운로드 오류입니다!"};
                    else
                        meal = ex.get(today.getDate() - 1).breakfast.split("\n");

                } else if (data.get("type").equals("중식")) { //점심 알람

                    builder.setContentTitle("중식 알림");
                    if (new_month[0])
                        meal = new String[]{"급식 다운로드 오류입니다!"};
                    else
                        meal = ex.get(today.getDate() - 1).lunch.split("\n");

                } else if (data.get("type").equals("석식")) { //저녁 알람
                    builder.setContentTitle("석식 알림");
                    if (new_month[0])
                        meal = new String[]{"급식 다운로드 오류입니다!"};
                    else
                        meal = ex.get(today.getDate() - 1).dinner.split("\n");

                }
                Notification.InboxStyle inboxStyle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    inboxStyle = new Notification.InboxStyle(builder);

                    for (int i = 0; i < meal.length; i++)
                        inboxStyle.addLine(meal[i]);
                    inboxStyle.setSummaryText("전체 보기");
                    builder.setStyle(inboxStyle);
                    notificationManager.notify(0, builder.build());
                }


            } else if (data.containsKey("month")) {// 일정알람
                String year = data.get("year"); // 년
                String month = data.get("month").length() == 1 ? "0" + data.get("month") : data.get("month"); // 월
                String date = data.get("date").length() == 1 ? "0" + data.get("date") : data.get("date"); // 월 // 일
                String title = remoteMessage.getNotification().getTitle(); //제목
                String msg = remoteMessage.getNotification().getBody(); //메세지
                Listsave.HomeCardList.add(title, year + "." + month + "." + date + ".", msg);
                sendNotification();
            }
        }
    }

    private void sendNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent go_main = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, go_main, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("my_notification_channel", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            nm.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_notification_channel");
        builder.setContentTitle("SNS 알림")
                .setContentText("새로운 공지가 추가되었습니다.")
                .setSmallIcon(R.drawable.bellxxxhdpi)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true);
        nm.notify(0, builder.build());
    }
}
