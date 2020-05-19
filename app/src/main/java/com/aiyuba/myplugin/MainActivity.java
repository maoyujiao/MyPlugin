package com.aiyuba.myplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 插桩式插件开发
 *
 * //反射获取对象，并注入该activity
 //加载apk文件，获取包名
 //并将资源文件挂在在asset目录下
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static final String ACTION = "com.aiyuba.plugin1.Receive.PLUGIN_ACTION";


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, " 我是宿主，收到你的消息,握手完成!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(mReceiver,new IntentFilter(ACTION));
    }

    public void loadPlugin(View view) {
        File file = new File(this.getDir("plugin", Context.MODE_PRIVATE),"plugin.apk");
        if(file.exists()){
            Log.d(TAG, "loadPlugin: " + "插件已经写入");
        }else {
            //将apk文件放进该目录
            File source = new File(Environment.getExternalStorageDirectory(), "plugin.apk");
            try (FileOutputStream outputStream = new FileOutputStream(file);
                 FileInputStream inputStream = new FileInputStream(source)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);//bug：注意此处的len，不然就加载失败。或许是多写入了未被替换的byte
                    Log.d(TAG, "loadPlugin: " + buffer.length);
                }

                if (file.exists()) {
                    Log.d(TAG, "loadPlugin: " + "插件已经写入");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PluginManager.getInstance().loadPath(this);
        Log.d(TAG, "loadPlugin: " + "资源加载成功");
    }

    public void jumpPlugin(View view) {
        Intent intent = new Intent(this,ProxyActivity.class);
        intent.putExtra("classname",PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);
    }

    public void sendBrocast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.aiyuba.plugin1.StaticReceiver");
        sendBroadcast(intent);
    }
}
