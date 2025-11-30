package com.smsforwarder;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class OverlayView extends LinearLayout {
    public OverlayView(Context c) {
        super(c);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.parseColor("#AA000000"));
        setPadding(16,16,16,16);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public void updateMessages(List<String> msgs, String status) {
        removeAllViews();
        TextView t = new TextView(getContext());
        t.setText("状态:"+status);
        t.setTextColor(Color.WHITE);
        addView(t);
        int n = Math.min(5, msgs.size());
        for (int i=0;i<n;i++) {
            TextView v = new TextView(getContext());
            v.setText(msgs.get(i));
            v.setTextColor(Color.WHITE);
            addView(v);
        }
    }
}
