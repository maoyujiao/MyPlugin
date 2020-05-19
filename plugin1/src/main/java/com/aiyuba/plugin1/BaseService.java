package com.aiyuba.plugin1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aiyuba.pluginstand.PayInterfaceService;

/**
 * Created by maoyujiao on 2020/5/19.
 */

public class BaseService extends Service  implements PayInterfaceService {

    private Service that;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        this.that = proxyService;
    }

    @Override
    public void onCreate() {

    }
}
