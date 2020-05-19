package com.aiyuba.pluginstand;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

/**
 * Created by maoyujiao on 2020/5/15.
 */

public interface PayInterfaceBroadcast {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);
}
