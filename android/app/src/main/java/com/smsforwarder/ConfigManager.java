package com.smsforwarder;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

public class ConfigManager {
    private static final String PREF = "config_pref";
    private static final String K_IV = "iv";
    private static final String K_CT = "ct";

    public void save(Context ctx, ConfigData data) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("serverUrl", data.getServerUrl());
        obj.put("phoneNumber", data.getPhoneNumber());
        String json = obj.toString();
        CryptoManager cm = new CryptoManager();
        String[] enc = cm.encrypt(json);
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(K_IV, enc[0]).putString(K_CT, enc[1]).apply();
    }

    public ConfigData load(Context ctx) throws Exception {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String iv = sp.getString(K_IV, null);
        String ct = sp.getString(K_CT, null);
        if (iv == null || ct == null) return null;
        CryptoManager cm = new CryptoManager();
        String json = cm.decrypt(iv, ct);
        JSONObject obj = new JSONObject(json);
        return new ConfigData(obj.optString("serverUrl"), obj.optString("phoneNumber"));
    }
}
