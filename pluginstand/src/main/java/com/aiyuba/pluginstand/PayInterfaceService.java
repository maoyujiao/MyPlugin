package com.aiyuba.pluginstand;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;

/**
 * Created by maoyujiao on 2020/5/15.
 */

public interface PayInterfaceService {

    public void onCreate();

    public void onStart(Intent intent, int startId);

    public int onStartCommand(Intent intent, int flags, int startId);

    public void onDestroy();

    public void onConfigurationChanged(Configuration newConfig);

    public void onLowMemory();

    public void onTrimMemory(int level);

    public IBinder onBind(Intent intent);

    public boolean onUnbind(Intent intent);

    public void onRebind(Intent intent);

    public void onTaskRemoved(Intent rootIntent);

    public void attach(Service proxyService);
}
