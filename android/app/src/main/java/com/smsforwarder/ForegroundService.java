package com.smsforwarder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import android.app.Service;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class ForegroundService extends Service {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int failCount = 0;
    private Runnable task = new Runnable() {
        public void run() {
            try {
                ConfigData cfg = new ConfigManager().load(ForegroundService.this);
                if (cfg != null) {
                    long ts = System.currentTimeMillis();
                    JSONObject body = new JSONObject();
                    String devId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    body.put("device_id", devId);
                    body.put("phone_number", cfg.getPhoneNumber());
                    body.put("timestamp", ts);
                    post(cfg.getServerUrl() + "/api/heartbeat", body.toString(), null);
                    failCount = 0;
                }
            } catch (Exception e) {
                failCount++;
            }
            long delay = 10000L;
            if (failCount > 0) delay = Math.min(10000L * (1L << Math.min(failCount,3)), 80000L);
            if (failCount <= 3) handler.postDelayed(this, delay);
            else handler.postDelayed(this, 10000L);
        }
    };

    public void onCreate() {
        createChannel();
        Intent i = new Intent(this, ConfigActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE);
        Notification n = new NotificationCompat.Builder(this, "sms_forwarder")
                .setContentTitle("短信转发器")
                .setContentText("运行中")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentIntent(pi)
                .build();
        startForeground(1, n);
        handler.post(task);
    }

    public int onStartCommand(Intent intent, int flags, int startId) { return START_STICKY; }
    public IBinder onBind(Intent intent) { return null; }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel("sms_forwarder", "短信转发", NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(ch);
        }
    }

    private void post(String url, String json, String token) throws Exception {
        URL u = new URL(url);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json");
        if (token != null) c.setRequestProperty("Authorization", "Bearer " + token);
        c.setDoOutput(true);
        byte[] b = json.getBytes("UTF-8");
        c.setFixedLengthStreamingMode(b.length);
        OutputStream os = c.getOutputStream();
        os.write(b);
        os.flush();
        os.close();
        c.getResponseCode();
        c.disconnect();
    }
}
