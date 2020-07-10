package com.rainwood.sentlogistics.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.ui.widget.LoadingView;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/23 17:09
 * @Desc: 推荐有奖
 */
public final class RecommendActivity extends BaseActivity {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageBack;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.iv_loading)
    private LoadingView mLoadingView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        StatusBarUtils.darkMode(this, false);
        pageTop.setBackgroundColor(Color.TRANSPARENT);
        pageBack.setImageResource(R.drawable.ic_page_back_white);
        pageTitle.setText(title);
        pageTitle.setTextColor(getColor(R.color.white));
        //

    }

    @Override
    protected void initPresenter() {

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
}
