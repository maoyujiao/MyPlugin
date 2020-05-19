package com.aiyuba.myplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by maoyujiao on 2020/5/15.
 */

public class PluginManager {
    private static final PluginManager ourInstance = new PluginManager();
    private DexClassLoader dexClassLoader;
    private Resources apkResource;
    private PackageInfo packageInfo;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }


    /**
     * 加载apk包到asset中
     * res 及dex
     *
     * @param context
     */
    public void loadPath(Context context) {
        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String apkPath = new File(filesDir, name).getAbsolutePath();
        PackageManager pm = context.getPackageManager();
        //packageInfo 按这种方式packageInfo是null 存粗权限打开
        packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);

        if (packageInfo == null) {
            throw new IllegalStateException("Extension APK could not be parsed");
        }

        //activity
        PathClassLoader pathClassLoader = (PathClassLoader)context.getClassLoader();
        File dex = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(apkPath, dex.getAbsolutePath(), null, pathClassLoader);

        // resource addAssetPath hide方法
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, apkPath);
            apkResource = new Resources(assetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }

        parseReceivers(context, apkPath);

    }

    /**
     * 解析插件中的静态广播
     * @param context
     * @param apkPath
     *
     * 获取PackageParse，来解析apk，进而获取Package中的reciver列表。
     * 根据generateActivityInfo方法反射来获取ActivityInfo,
     * 根据ActivityInfo获取Name进而反射得到BroadcastReciver
     * 反射获取Compont中的intent，遍历reciver可以获取IntentFilters，进而注册广播
     *
     * generateActivityInfo方法需要4个参数。Activity，int,PackageUserState,userId
     */
    private void parseReceivers(Context context, String apkPath) {
        try {
            //获取PackageParser,进而解析apk
            Class packageParseClass = Class.forName("android.content.pm.PackageParser");
            Object packageParserObj = packageParseClass.newInstance();

            //解析apk获取package对象。parsePackage方法 返回 Package对象
            Method parsePackage = packageParseClass.getDeclaredMethod("parsePackage", File.class,int.class);
            Object packageObj = parsePackage.invoke(packageParserObj,new File(apkPath),PackageManager.GET_ACTIVITIES);

            //从Package中获取recivers属性
            Field receiverField = packageObj.getClass().getDeclaredField("receivers");
            List receivers = (List) receiverField.get(packageObj);

            //先获取组件中声明的intent，进而就可以将Reciver中的intentFilter。
            // Component中的intents属性
            Class compontClass = Class.forName("android.content.pm.PackageParser$Component");
            Object compontObj =  compontClass.newInstance();
            Field intentField = compontObj.getClass().getDeclaredField("intents");

            // Reciver的格式和acitivty相同，都包含IntentFilter
            //获取PackageParse中的Activity字节码
            Class packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");

            //PackageUserState对象不知道是干啥的
            Class packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object defaltUserState =  packageUserStateClass.newInstance();

            //反射获取generateActivityInfo方法
            Method generateActivityInfo = packageParseClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass,
                    int.class,
                    packageUserStateClass,
                    int.class);

            //反射获取userid
            Class userHandler = Class.forName("android.os.UserHandle");
            Object userHandlerObj = userHandler.newInstance();
            Method getCallingUserId = userHandler.getDeclaredMethod("getCallingUserId");
            int userid = (int)getCallingUserId.invoke(userHandlerObj,null); // bug 应该需要加上Obj

            for (Object activity : receivers) {
                //对于每个reciver反射获取ActivityInfo
                ActivityInfo info = (ActivityInfo) generateActivityInfo.invoke(
                        packageParserObj,
                        activity,
                        0,
                        defaltUserState,
                        userid
                );
                //并获取其下的intentFiltetr
                List<? extends IntentFilter> intentFilters = (List<? extends IntentFilter>) intentField.get(activity);
                //类加载器加载该reciver，并获取该实例
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) dexClassLoader.loadClass(info.name).newInstance();
                //注册改广播下所有的intentFliter
                for (IntentFilter intentFilter : intentFilters) {
                    context.registerReceiver(broadcastReceiver,intentFilter);
                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getApkResource() {
        return apkResource;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }
}
