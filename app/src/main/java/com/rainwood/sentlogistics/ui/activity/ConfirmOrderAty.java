package com.rainwood.sentlogistics.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/7/10 17:09
 * @Desc: 确认订单
 */
public final class ConfirmOrderAty extends BaseActivity {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {

    }
}
