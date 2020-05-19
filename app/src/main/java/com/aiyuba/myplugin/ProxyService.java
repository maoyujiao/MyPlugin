package com.aiyuba.myplugin;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aiyuba.pluginstand.PayInterfaceService;

import java.lang.reflect.Constructor;

/**
 * Created by maoyujiao on 2020/5/19.
 */

public class ProxyService extends Service {
    private PayInterfaceService payInterfaceService;
    private String serviceName;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        serviceName = intent.getStringExtra("serviceName");
        //加载service 类
        try {
            //插件oneService
            Class<?> aClass = getClassLoader().loadClass(serviceName);
            Constructor constructor = aClass.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});

            payInterfaceService = (PayInterfaceService) in;
            payInterfaceService.attach(this);

            Bundle bundle = new Bundle();
            bundle.putInt("from", 1);
            payInterfaceService.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(payInterfaceService == null){
            init(intent);
        }
        return payInterfaceService.onStartCommand(intent, flags, startId);
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader(); //bug 忘记重写getClassLoader
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getApkResource();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        payInterfaceService.onUnbind(intent);
        return super.onUnbind(intent);
    }
}
