package com.aiyuba.plugin1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by maoyujiao on 2020/5/19.
 *
 * 静态广播。在没有new 这个广播的情况下，能接收到该广播
 */

public class StaticReceiver extends BroadcastReceiver {
    static final String ACTION = "com.aiyuba.plugin1.Receive.PLUGIN_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.aiyuba.plugin1.StaticReceiver".equals(intent.getAction())){
            Log.d("StaticReceiver", "我是插件   收到宿主的消息  静态注册的广播  收到宿主的消息");
        }
        context.sendBroadcast(new Intent(ACTION));
    }
}
