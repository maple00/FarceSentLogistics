package com.rainwood.sentlogistics.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseFragment;
import com.rainwood.sentlogistics.model.domain.BulletinData;
import com.rainwood.sentlogistics.persenter.IViewPagerPresenter;
import com.rainwood.sentlogistics.ui.adapter.BulletinContentAdapter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.sentlogistics.view.IViewPagerCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:51
 * @Desc: 消息公告 pager
 */
public final class BulletinFragment extends BaseFragment implements IViewPagerCallbacks {

    @ViewInject(R.id.trl_bulletin_refresh)
    private TwinklingRefreshLayout bulletinRefresh;
    @ViewInject(R.id.rv_bulletin_content_list)
    private RecyclerView bulletinContentView;

    private IViewPagerPresenter mViewPagerPresenter;
    private BulletinContentAdapter mContentAdapter;
    private String mTypes;

    public static Fragment newInstance(String type) {
        BulletinFragment bulletinFragment = new BulletinFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BULLETIN_TITLE, type);
        bulletinFragment.setArguments(bundle);
        return bulletinFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_bulletin;
    }

    @Override
    protected void initView(View rootView) {
        // 设置布局管理器
        bulletinContentView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        bulletinContentView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mContentAdapter = new BulletinContentAdapter();
        // 设置适配器
        bulletinContentView.setAdapter(mContentAdapter);
        // 设置刷新属性
        bulletinRefresh.setEnableLoadmore(false);
        bulletinRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mViewPagerPresenter = PresenterManager.getOurInstance().getViewPagerPresenter();
        mViewPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        mTypes = arguments.getString(Constants.KEY_BULLETIN_TITLE);
        if (mViewPagerPresenter != null) {
            mViewPagerPresenter.requestContentByType(mTypes);
        }
    }

    @Override
    public String getBulletinType() {
        return mTypes;
    }

    /**
     * 获取内容列表
     *
     * @param bulletinList
     */
    @Override
    public void getBulletinData(List<BulletinData> bulletinList) {
        setUpState(State.SUCCESS);
        mContentAdapter.setBulletinList(bulletinList);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        toast("当前没有" + getBulletinType());
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        if (mViewPagerPresenter != null) {
            mViewPagerPresenter.unregisterViewCallback(this);
        }
    }
}
