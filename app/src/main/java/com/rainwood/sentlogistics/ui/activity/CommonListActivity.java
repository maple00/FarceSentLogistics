package com.rainwood.sentlogistics.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.ActivityList;
import com.rainwood.sentlogistics.persenter.ICommonListPresenter;
import com.rainwood.sentlogistics.ui.adapter.EventListAdapter;
import com.rainwood.sentlogistics.utils.PageJumpUtil;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.sentlogistics.view.ICommonListCallback;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:10
 * @Desc: 公共列表页面(活动列表 、)
 */
public final class CommonListActivity extends BaseActivity implements ICommonListCallback {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.trl_page_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_data_list)
    private RecyclerView dataListView;

    private ICommonListPresenter mCommonListPresenter;
    private EventListAdapter mEventListAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        dataListView.setLayoutManager(new GridLayoutManager(this, 1));
        dataListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mEventListAdapter = new EventListAdapter();
        // 设置适配器
        dataListView.setAdapter(mEventListAdapter);
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(false);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mCommonListPresenter = PresenterManager.getOurInstance().getCommonListPresenter();
        mCommonListPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求活动列表
        mCommonListPresenter.requestEventsListData();
    }

    @Override
    protected void initEvent() {
        mEventListAdapter.setClickEventsListener(new EventListAdapter.OnClickEventsListener() {
            @Override
            public void onClickItem(ActivityList event, int position) {
                PageJumpUtil.activities2Detail(CommonListActivity.this, PageBrowserActivity.class, event.getId());
            }
        });
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

    /**
     * 活动列表
     *
     * @param activityList
     */
    @Override
    public void getActivityList(List<ActivityList> activityList) {
        mEventListAdapter.setEventLists(activityList);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
