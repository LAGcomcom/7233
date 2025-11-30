package com.smsforwarder;

import android.content.Context;
import android.os.Build;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

public class CryptoManager {
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "config_key";

    private SecretKey getOrCreateKey() throws Exception {
        javax.crypto.SecretKey key;
        try {
            java.security.KeyStore ks = java.security.KeyStore.getInstance(ANDROID_KEYSTORE);
            ks.load(null);
            Key existing = ks.getKey(KEY_ALIAS, null);
            if (existing instanceof SecretKey) return (SecretKey) existing;
        } catch (Exception ignored) {}
        KeyGenerator kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build();
        kg.init(spec);
        key = kg.generateKey();
        return key;
    }

    public String[] encrypt(String plain) throws Exception {
        SecretKey key = getOrCreateKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] ct = cipher.doFinal(plain.getBytes("UTF-8"));
        return new String[]{Base64.encodeToString(iv, Base64.NO_WRAP), Base64.encodeToString(ct, Base64.NO_WRAP)};
    }

    public String decrypt(String ivB64, String ctB64) throws Exception {
        SecretKey key = getOrCreateKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = Base64.decode(ivB64, Base64.NO_WRAP);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] pt = cipher.doFinal(Base64.decode(ctB64, Base64.NO_WRAP));
        return new String(pt, "UTF-8");
    }
}
