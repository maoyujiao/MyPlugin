package com.aiyuba.plugin1;


import android.util.Log;

/**
 * Created by maoyujiao on 2020/5/19.
 */

public class OneService extends BaseService {
    private static final String TAG = "OneService";
    private int i = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    Log.i(TAG, "run: "+(i++));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
