package com.smsforwarder;

public interface SmsListener {
    void onSms(String sender, String content, long receiveTime);
}
