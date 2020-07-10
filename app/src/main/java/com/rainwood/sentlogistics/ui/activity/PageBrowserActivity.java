package com.rainwood.sentlogistics.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.persenter.ICommonDetailPresenter;
import com.rainwood.sentlogistics.ui.widget.BrowserView;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.view.ICommonDetailCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/23 13:07
 * @Desc: 详情页面（活动详情、）
 */
public final class PageBrowserActivity extends BaseActivity implements ICommonDetailCallback {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.wv_browser_view)
    private BrowserView mBrowserView;

    private ICommonDetailPresenter mDetailPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_page_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mDetailPresenter = PresenterManager.getOurInstance().getDetailPresenter();
        mDetailPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String detailId = getIntent().getStringExtra("detailId");
        switch (rightTitle) {
            case "activityDetail":
                mDetailPresenter.requestDetailByAtyId(detailId);
                break;
        }
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
    public void getActivityDetailData(String data) {
        mBrowserView.loadData(data, "", "");
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBrowserView.canGoBack()) {
            // 后退网页并且拦截该事件
            mBrowserView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        mBrowserView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mBrowserView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBrowserView.onDestroy();
        super.onDestroy();
    }

}
