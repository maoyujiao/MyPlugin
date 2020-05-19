package com.aiyuba.pluginstand;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by maoyujiao on 2020/5/15.
 */

public interface PayInterfaceActivity {

    public void attach(Activity proxyActivity);

    public void onCreate(Bundle bundle);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestroy();
    public void onSaveInstanceState(Bundle outState);
    public boolean onTouchEvent(MotionEvent event);
    public void onBackPressed();
}
