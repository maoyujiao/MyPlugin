package com.aiyuba.plugin1;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aiyuba.pluginstand.PayInterfaceActivity;

public class PluginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        findViewById(R.id.jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that,"插件",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(that,SecondActivity.class));
                startService(new Intent(that, OneService.class));
            }
        });

        IntentFilter intentFilter = new IntentFilter("com.aiyuba.plugin1.PluginActivity");
        registerReceiver(new MyReceiver(),intentFilter);

        findViewById(R.id.sendBroadCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.aiyuba.plugin1.PluginActivity");
                sendBroadcast(intent);
            }
        });
    }


}
