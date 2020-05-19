package com.aiyuba.myplugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aiyuba.pluginstand.PayInterfaceActivity;

import java.lang.reflect.Constructor;

/**
 * Created by maoyujiao on 2020/5/15.
 * 插桩化插件开发，插桩的代理类
 */

public class ProxyActivity extends Activity{
    private static final String TAG = "ProxyActivity";
    private PayInterfaceActivity payInterfaceActivity;
    private String className;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity的启动会走onCreate
        className = getIntent().getStringExtra("classname");
        //反射创建activity对象，并将proxy注入
        try {
            Class<?> clazz = getClassLoader().loadClass(className);
            Constructor constructor = clazz.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});
            payInterfaceActivity = (PayInterfaceActivity)in;
            payInterfaceActivity.attach(this);
            Bundle bundle = new Bundle();
            payInterfaceActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader(); //bug 忘记重写getClassLoader
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getApkResource();
    }


    //
    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className);
        super.startActivity(intent1);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("serviceName");
        Intent service1 = new Intent(this, ProxyService.class);
        service1.putExtra("serviceName", className);
        return super.startService(service1);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(new ProxyBroadReceiver(receiver.getClass().getName(),
                this), filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        payInterfaceActivity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        payInterfaceActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        payInterfaceActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        payInterfaceActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        payInterfaceActivity.onDestroy();
    }
}
