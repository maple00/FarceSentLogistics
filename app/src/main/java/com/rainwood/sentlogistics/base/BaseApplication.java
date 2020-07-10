package com.rainwood.sentlogistics.base;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.rainwood.imageselector.ImageSelector;
import com.rainwood.sentlogistics.helper.ActivityStackManager;
import com.rainwood.sentlogistics.network.okhttp.HttpCacheBody;
import com.rainwood.sentlogistics.network.sqlite.SQLiteHelper;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:52
 * @Desc: app
 */
public final class BaseApplication extends Application {

    private static final String TAG = "sxs";
    /**
     * 是否调试
     */
    private boolean isDebug = false;
    /**
     * 是否显示
     */
    private boolean isShow = false;

    /**
     * application 对象
     */
    public static BaseApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        // 程序创建的时候执行
        LogUtil.d(TAG, "onCreate");
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        app = this;
        // initActivity 初始化Activity 栈管理
        initActivity();
        // 初始化三方的框架
        initSDK();
        // 初始化工具类
        initUtil();
        // 初始化数据库
        initDB();
    }

    private void initSDK() {
        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);
        // 初始化图片选择器
        ImageSelector.init();
    }

    /**
     * 初始化工具类
     */
    private void initUtil() {
        // 获取IMEI

    }

    /**
     * 创建数据表
     */
    private void initDB() {
        // 创建历史收/发货地址 table
        // SQLiteHelper.with(this).dropTable("historyAddress");
        SQLiteHelper.with(this).createTable("historyAddress", new String[]{"name", "address", "latX", "lngY", "time"});
        // 创建缓存表
        SQLiteHelper.with(this).createTable(HttpCacheBody.class);
    }

    private void initActivity() {
        ActivityStackManager.getInstance().init(app);
    }

    public boolean isDetermineNetwork() {
        return true;
    }

    /**
     * 是否是调试环境
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置调试模式
     *
     * @param debug
     */
    public void setDebugMode(boolean debug) {
        isDebug = debug;
        isShow = false;
    }

    /**
     * 设置调试环境
     *
     * @param enable
     * @param show   显示
     */
    public void setDebugMode(boolean enable, boolean show) {
        isDebug = enable;
        isShow = show;
    }

    /**
     * @return
     */
    public boolean isShowDebug() {
        return isShow;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtil.d(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtil.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行（回收内存）
        // HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        LogUtil.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
