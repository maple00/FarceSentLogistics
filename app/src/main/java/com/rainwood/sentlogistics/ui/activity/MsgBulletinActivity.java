package com.rainwood.sentlogistics.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.persenter.ITabLayoutPresenter;
import com.rainwood.sentlogistics.ui.adapter.BulletinTypeAdapter;
import com.rainwood.sentlogistics.ui.widget.GlueTabLayout;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.view.ITabLayoutCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;
import static com.rainwood.tools.utils.SmartUtil.dp2px;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:13
 * @Desc: 消息公告
 */
public final class MsgBulletinActivity extends BaseActivity implements ITabLayoutCallback {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.gtl_indicator)
    private GlueTabLayout mBulletinTabLayout;
    @ViewInject(R.id.vp_bulletin_pager)
    private ViewPager bulletinPager;

    private ITabLayoutPresenter mTabLayoutPresenter;
    private BulletinTypeAdapter mBulletinTypeAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_msg_bulletin;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 适配器属性
        mBulletinTabLayout.setupWithViewPager(bulletinPager);
        mBulletinTypeAdapter = new BulletinTypeAdapter(getSupportFragmentManager());
        // 设置TabLayout属性
        mBulletinTabLayout.setTabMode(GlueTabLayout.GRAVITY_CENTER);
        // 设置指示器下划线高度和颜色
        mBulletinTabLayout.setSelectedTabIndicatorHeight(dp2px(3));
        mBulletinTabLayout.setSelectedTabIndicatorColor(getContext().getColor(R.color.colorPrimary));
        //GlueTabLayout 设置下划线指示器圆角
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(2));
        mBulletinTabLayout.setSelectedTabIndicator(gradientDrawable);
        //GlueTabLayout 设置点击动画为水波纹扩散效果
        mBulletinTabLayout.setUnboundedRipple(false);
        //GlueTabLayout 设置下划线指示器的宽度
        mBulletinTabLayout.setTabIndicatorWidth(0.1f);
        // 设置点击时的背景
        mBulletinTabLayout.setTabRippleColor(ColorStateList.valueOf(getContext().getColor(R.color.transparent)));
        // 设置字体颜色字体
        mBulletinTabLayout.setTabTextColors(getContext().getColor(R.color.fontColor), getContext().getColor(R.color.fontColor));
        // 设置适配器
        bulletinPager.setAdapter(mBulletinTypeAdapter);
    }

    @Override
    protected void initPresenter() {
        mTabLayoutPresenter = PresenterManager.getOurInstance().getTabLayoutPresenter();
        mTabLayoutPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mTabLayoutPresenter.requestBulletinType();
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getBulletinTypes(List<String> bulletinList) {
        // 数据从这里回来
        if (mBulletinTypeAdapter != null) {
            mBulletinTypeAdapter.setBulletinList(bulletinList);
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mTabLayoutPresenter != null) {
            mTabLayoutPresenter.unregisterViewCallback(this);
        }
    }
}
