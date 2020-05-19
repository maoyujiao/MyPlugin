package com.aiyuba.plugin1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aiyuba.pluginstand.PayInterfaceBroadcast;

/**
 * Created by maoyujiao on 2020/5/19.
 */

public class MyReceiver extends BroadcastReceiver implements PayInterfaceBroadcast {
    private static final String TAG = "BaseBroadcast";
    private Context that;
    @Override
    public void attach(Context context) {
        Toast.makeText(context, "-----绑定上下文成功-----", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "attach: " + "-----绑定上下文成功-----");


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "-----插件收到广播-----", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: " + "-----插件收到广播-----");

    }
}
