package com.aiyuba.myplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.aiyuba.pluginstand.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;

/**
 * Created by maoyujiao on 2020/5/19.
 */

public class ProxyBroadReceiver extends BroadcastReceiver {
    private PayInterfaceBroadcast payInterfaceBroadcast;

    public ProxyBroadReceiver() {
    }

    public ProxyBroadReceiver(String className, Context context) {

        try {
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor constructor = aClass.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});
            payInterfaceBroadcast = (PayInterfaceBroadcast) in;
            payInterfaceBroadcast.attach(context);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context,intent);

    }

    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader(); //bug 忘记重写getClassLoader
    }

    public Resources getResources() {
        return PluginManager.getInstance().getApkResource();
    }


}
