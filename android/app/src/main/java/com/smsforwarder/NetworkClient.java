package com.smsforwarder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import android.util.Base64;

public class NetworkClient {
    public int postJson(String url, String json, String token, String hmacSecret) throws Exception {
        URL u = new URL(url);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json");
        if (token != null && token.length()>0) c.setRequestProperty("Authorization", "Bearer " + token);
        if (hmacSecret != null && hmacSecret.length()>0) {
            String sig = hmacSha256(json, hmacSecret);
            c.setRequestProperty("X-Signature", sig);
        }
        c.setDoOutput(true);
        byte[] b = json.getBytes("UTF-8");
        c.setFixedLengthStreamingMode(b.length);
        OutputStream os = c.getOutputStream();
        os.write(b);
        os.flush();
        os.close();
        int code = c.getResponseCode();
        c.disconnect();
        return code;
    }
    private String hmacSha256(String data, String key) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec ks = new javax.crypto.spec.SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        mac.init(ks);
        byte[] h = mac.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte x : h) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}
