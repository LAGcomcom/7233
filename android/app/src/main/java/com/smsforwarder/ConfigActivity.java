package com.smsforwarder;

import android.os.Bundle;
import android.util.Patterns;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Button;
import java.util.regex.Pattern;

public class ConfigActivity extends AppCompatActivity {
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://)?([\\w.-]+)(:\\d+)?(/.*)?$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        TextInputLayout tilUrl = findViewById(R.id.til_server_url);
        TextInputLayout tilPhone = findViewById(R.id.til_phone_number);
        TextInputEditText etUrl = findViewById(R.id.et_server_url);
        TextInputEditText etPhone = findViewById(R.id.et_phone_number);
        Button btn = findViewById(R.id.btn_save_config);

        etUrl.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {}
            public void afterTextChanged(android.text.Editable s) {
                String v = s.toString();
                boolean ok = URL_PATTERN.matcher(v).matches();
                tilUrl.setError(ok ? null : "地址格式错误");
            }
        });
        etPhone.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {}
            public void afterTextChanged(android.text.Editable s) {
                String v = s.toString();
                boolean ok = PHONE_PATTERN.matcher(v).matches();
                tilPhone.setError(ok ? null : "号码格式错误");
            }
        });
        btn.setOnClickListener(v -> {
            String url = etUrl.getText() == null ? "" : etUrl.getText().toString();
            String phone = etPhone.getText() == null ? "" : etPhone.getText().toString();
            boolean okUrl = URL_PATTERN.matcher(url).matches();
            boolean okPhone = PHONE_PATTERN.matcher(phone).matches();
            tilUrl.setError(okUrl ? null : "地址格式错误");
            tilPhone.setError(okPhone ? null : "号码格式错误");
            if (okUrl && okPhone) {
                try {
                    new ConfigManager().save(this, new ConfigData(url, phone));
                    android.widget.Toast.makeText(this, "已保存", android.widget.Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    android.widget.Toast.makeText(this, "保存失败", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
