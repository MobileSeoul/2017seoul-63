package com.noti.sns.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Connection {
    public static String[] sendJSON(String url, String data) {
        final String[] s = new String[1];
        Thread t = new Thread(() -> {
            try {
                URL url1 = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setConnectTimeout(3000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(data);
                osw.flush();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer sb = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                s[0] = sb.toString();
            } catch (ProtocolException e) {
                s[0] = "ProtocolException";
            } catch (MalformedURLException e) {
                s[0] = "MalformedURLException";
            } catch (IOException e) {
                s[0] = "IOException";
            } catch (Exception e) {
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s[0].split(",");
    }

}
