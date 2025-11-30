package com.smsforwarder;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.content.Context;

public class SmsObserver extends ContentObserver {
    private final Context ctx;
    private final SmsListener listener;
    private long lastId = -1;

    public SmsObserver(Handler h, Context c, SmsListener l) {
        super(h);
        this.ctx = c;
        this.listener = l;
    }

    public void onChange(boolean selfChange) {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = ctx.getContentResolver().query(uri, new String[]{"_id","address","body","date"}, null, null, "date DESC LIMIT 1");
        if (cursor == null) return;
        try {
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(0);
                String address = cursor.getString(1);
                String body = cursor.getString(2);
                long date = cursor.getLong(3);
                if (id != lastId) {
                    lastId = id;
                    if (listener != null) listener.onSms(address, body, date);
                }
            }
        } finally {
            cursor.close();
        }
    }
}
