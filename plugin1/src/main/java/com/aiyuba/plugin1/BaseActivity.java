package com.aiyuba.plugin1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aiyuba.pluginstand.PayInterfaceActivity;

/**
 * Created by maoyujiao on 2020/5/15.
 */

public class BaseActivity extends Activity implements PayInterfaceActivity {
    Activity that;
    private static final String TAG = "BaseActivity";
    @Override
    public void attach(Activity proxyActivity) {
        that = proxyActivity;

    }

    @Override
    public void setContentView(View view) {
        if(that != null){
            that.setContentView(view);
        }else{
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if(that != null){
            that.setContentView(layoutResID);
        }else{
            super.setContentView(layoutResID);
        }
    }

    //apk中启动activity，没有上下文，必须用that来辅助启动
    @Override
    public void startActivity(Intent intent) {
        if(that != null){
            Intent m  = new Intent();
            m.putExtra("className",intent.getComponent().getClassName());
            Log.d(TAG, "startActivity: " + intent.getComponent().getClassName());
            that.startActivity(m);
        }else {
            super.startActivity(intent);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if(that != null){
            Intent m = new Intent();
            m.putExtra("serviceName", service.getComponent().getClassName());
            return that.startService(m);
        }
        return super.startService(service);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if(that != null){
            that.sendBroadcast(intent);
        }
        super.sendBroadcast(intent);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if(that != null){
            that.registerReceiver(receiver,filter);
        }
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public View findViewById(int id) {
        if(that != null){
            return that.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        if(that!=null){
            return that.getIntent();
        }
        return super.getIntent();
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if(that != null) {
            return that.getLayoutInflater();
        }
        return super.getLayoutInflater();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if(that != null) {
            return that.getApplicationInfo();
        }
        return super.getApplicationInfo();
    }


    @Override
    public Window getWindow() {
        if(that != null) {
            return that.getWindow();
        }
        return super.getWindow();
    }


    @Override
    public WindowManager getWindowManager() {
        if(that != null) {
            return that.getWindowManager();
        }
        return super.getWindowManager();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {
//        super.onStart();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
//        super.onResume();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {
//        super.onPause();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {
//        super.onStop();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
//        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);

    }
}
