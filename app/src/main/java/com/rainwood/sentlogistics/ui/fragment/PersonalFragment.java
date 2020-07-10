package com.rainwood.sentlogistics.ui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseFragment;
import com.rainwood.sentlogistics.ui.activity.MsgBulletinActivity;
import com.rainwood.sentlogistics.ui.activity.PersonalDataActivity;
import com.rainwood.sentlogistics.ui.activity.RecommendActivity;
import com.rainwood.sentlogistics.ui.activity.TokenOrderActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/22 15:28
 * @Desc: 个人中心
 */
public final class PersonalFragment extends BaseFragment {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        StatusBarUtils.setMargin(getContext(), pageTop);
        //
    }

    @SingleClick
    @OnClick({R.id.rl_page_top, R.id.tv_token_order, R.id.iv_token_order, R.id.iv_mine_order, R.id.tv_mine_order,
            R.id.tv_store_transport, R.id.iv_store_transport, R.id.tv_mine_share, R.id.iv_mine_share,
            R.id.tv_call_center, R.id.iv_call_center, R.id.tv_feedback, R.id.iv_feedback,
            R.id.iv_more_setting, R.id.tv_more_setting, R.id.iv_recommend, R.id.tv_recommend, R.id.iv_msg,
            R.id.tv_msg, R.id.iv_purse, R.id.tv_purse, R.id.iv_route, R.id.tv_route})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_page_top:
                startActivity(getNewIntent(getContext(), PersonalDataActivity.class, "我的资料", "保存"));
                break;
            case R.id.iv_msg:
            case R.id.tv_msg:
                startActivity(getNewIntent(getContext(), MsgBulletinActivity.class, "消息公告", "recommend"));
                break;
            case R.id.iv_recommend:
            case R.id.tv_recommend:
                startActivity(getNewIntent(getContext(), RecommendActivity.class, "推荐有奖", "recommend"));
                break;
            case R.id.iv_purse:
            case R.id.tv_purse:
                toast("我的钱包");
                break;
            case R.id.iv_route:
            case R.id.tv_route:
                toast("常用路线");
                break;
            case R.id.iv_token_order:
            case R.id.tv_token_order:
                startActivity(getNewIntent(getContext(), TokenOrderActivity.class, "下单", "tokenOrder"));
                break;
            case R.id.iv_mine_order:
            case R.id.tv_mine_order:
                toast("我的订单");
                break;
            case R.id.tv_store_transport:
            case R.id.iv_store_transport:
                toast("门店运输情况");
                break;
            case R.id.tv_mine_share:
            case R.id.iv_mine_share:
                toast("我的分享");
                break;
            case R.id.tv_call_center:
            case R.id.iv_call_center:
                toast("客服中心");
                break;
            case R.id.tv_feedback:
            case R.id.iv_feedback:
                toast("意见反馈");
                break;
            case R.id.iv_more_setting:
            case R.id.tv_more_setting:
                toast("更多设置");
                break;
        }
    }
}
