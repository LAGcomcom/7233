package com.smsforwarder;

public class ConfigData {
    private String serverUrl;
    private String phoneNumber;

    public ConfigData(String serverUrl, String phoneNumber) {
        this.serverUrl = serverUrl;
        this.phoneNumber = phoneNumber;
    }

    public String getServerUrl() { return serverUrl; }
    public String getPhoneNumber() { return phoneNumber; }
}
