package com.noti.sns.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.noti.sns.R;
import com.noti.sns.server.Connection;

import org.json.JSONObject;

public class FirebaseInstanceIdServiceListener extends com.google.firebase.iid.FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences pref;
        pref = getSharedPreferences("save", 0);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString("fcm", refreshedToken);
        ed.apply();
    }

}
