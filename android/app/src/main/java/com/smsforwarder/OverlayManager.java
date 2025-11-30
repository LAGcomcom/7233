package com.smsforwarder;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

public class OverlayManager {
    private final Context ctx;
    private final WindowManager wm;
    private OverlayView view;
    private final List<String> cache = new ArrayList<>();
    private String status = "未知";
    public OverlayManager(Context c) { ctx=c; wm=(WindowManager)c.getSystemService(Context.WINDOW_SERVICE); }
    public void show() {
        if (view!=null) return;
        view = new OverlayView(ctx);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.TOP|Gravity.END;
        lp.x = 32; lp.y = 32;
        wm.addView(view, lp);
        view.updateMessages(cache, status);
    }
    public void hide() { if (view!=null) { wm.removeView(view); view=null; } }
    public void updateStatus(String s) { status=s; if(view!=null)view.updateMessages(cache, status); }
    public void pushMessage(String m) {
        cache.add(0,m);
        if (cache.size()>5) cache.remove(cache.size()-1);
        if(view!=null)view.updateMessages(cache, status);
    }
}
