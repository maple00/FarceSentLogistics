package com.rainwood.sentlogistics.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.rainwood.imageselector.ImageSelector;
import com.rainwood.imageselector.OnImageSelectListener;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/7/6 17:28
 * @Desc: 个人信息
 */
public final class PersonalDataActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView rightPageTitle;
    // content
    @ViewInject(R.id.iv_head_photo)
    private AppCompatImageView mViewHeadPhoto;
    private ImageSelector mSelector;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        rightPageTitle.setText(rightTitle);
        rightPageTitle.setTextColor(getColor(R.color.colorPrimary));

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSelector != null) {
            mSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title, R.id.fl_head_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                toast("保存了");
                finish();
                break;
            case R.id.fl_head_photo:
                //显示图片选择器
                ImageSelector.Builder builder = new ImageSelector.Builder(this);
                builder.crop(false);
                builder.size(300);
                builder.listener(new OnImageSelectListener() {
                    @Override
                    public void onImageSelectSucceed(Uri uri) {
                        LogUtil.d("sxs", "-- uri -- " + uri);
                    }

                    @Override
                    public void onImageSelectFailed(String msg) {
                        toast(msg);
                    }
                });
                mSelector = builder.build();
                break;
        }
    }

}
