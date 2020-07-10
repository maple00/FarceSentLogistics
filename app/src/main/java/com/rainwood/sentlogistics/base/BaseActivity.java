package com.rainwood.sentlogistics.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.service.autofill.OnClickAction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.sentlogistics.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.action.HandlerAction;

import java.lang.reflect.Method;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:16
 * @Desc: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickAction, HandlerAction {

    // public String TAG = this.getClass().getSimpleName();
    public final String TAG = "sxs";
    protected String title;
    protected String rightTitle;

    private State currentState = State.NONE;
    protected View mLoadingView;
    protected View mSuccessView;
    protected View mErrorView;
    protected View mEmptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    public void setUpState(State state) {
        this.currentState = state;
        /*mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);*/
    }

    // 需要重写OnCreate的Activity调用
    private Bundle savedInstanceState;

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ViewBind.inject(this);
        title = getIntent().getStringExtra("title") == null ? "" : getIntent().getStringExtra("title");
        rightTitle = getIntent().getStringExtra("rightTitle") == null ? "" : getIntent().getStringExtra("rightTitle");
        this.savedInstanceState = savedInstanceState;
        setStatusBar();
        initView();
        initEvent();
        initPresenter();
        initData();
        loadData();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置默认的状态栏背景颜色
     */
    protected void setStatusBar() {
        // StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.white));
        StatusBarUtils.darkMode(this);
        StatusBarUtils.immersive(this);
    }

    /**
     * event initial，需要时覆写
     */
    protected void initEvent() {

    }

    /**
     * 布局initial，需要时覆写
     */
    protected void initView() {

    }

    protected void loadData() {
        //加载数据
    }

    /**
     * 子类需要释放资源，覆盖即可
     */
    protected void release() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.release();
    }

    /**
     * 覆写子类布局
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * presenter 管理
     */
    protected abstract void initPresenter();

    /**
     * 获取当前 Activity 对象
     */
    public BaseActivity getActivity() {
        return this;
    }

    /**
     * startActivity 优化
     */
    protected Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title, String menu) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        intent.putExtra("menu", menu);
        return intent;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * 延迟执行
     */
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public final Object mHandlerToken = hashCode();

    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime(r, mHandlerToken, uptimeMillis);
    }

    /**
     * 设置软键盘自动调起
     */
    protected void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * startActivity 方法简化
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    /**
     * startActivityForResult 方法优化
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        hideSoftKeyboard();
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 使editText点击外部时候失去焦点
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    hideSoftKeyboard();
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    EditText editText = null;

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 设置上下平移的动画
     *
     * @param view
     */
    protected void setUpDownAnimation(View view) {
        TranslateAnimation translateAnimation;
        if (view.getVisibility() == View.VISIBLE) {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.5f);
        }
        translateAnimation.setDuration(300);
        view.setAnimation(translateAnimation);
    }

    /**
     * 判断手机是否有底部虚拟键位
     *
     * @param context
     * @return
     */
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            @SuppressLint("PrivateApi")
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("0".equals(navBarOverride)) {
                //不存在虚拟按键
                hasNavigationBar = false;
            } else if ("1".equals(navBarOverride)) {
                //存在虚拟按键
                hasNavigationBar = true;
            }
            Log.d(TAG, " 虚拟键位 ========== " + hasNavigationBar);
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 设置必填信息
     *
     * @param requestedText text
     * @param s             value
     */
    public void setRequiredValue(TextView requestedText, String s) {
        requestedText.setText(Html.fromHtml("<font color=" + getColor(R.color.middleColor)
                + " size=" + FontSwitchUtil.dip2px(this, 16f) + ">" + s + "</font>" +
                "<font color=" + getColor(R.color.red05) + " size= "
                + FontSwitchUtil.dip2px(this, 13f) + ">*</font>"));
    }

}
