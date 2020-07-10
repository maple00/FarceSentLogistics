package com.rainwood.sentlogistics.helper;

import com.rainwood.sentlogistics.persenter.ICommonDetailPresenter;
import com.rainwood.sentlogistics.persenter.ICommonListPresenter;
import com.rainwood.sentlogistics.persenter.IMapPresenter;
import com.rainwood.sentlogistics.persenter.ITabLayoutPresenter;
import com.rainwood.sentlogistics.persenter.ITokenOrderPresenter;
import com.rainwood.sentlogistics.persenter.IViewPagerPresenter;
import com.rainwood.sentlogistics.persenter.impl.CommonDetailImpl;
import com.rainwood.sentlogistics.persenter.impl.CommonListImpl;
import com.rainwood.sentlogistics.persenter.impl.MapImpl;
import com.rainwood.sentlogistics.persenter.impl.TabLayoutImpl;
import com.rainwood.sentlogistics.persenter.impl.TokenOrderImpl;
import com.rainwood.sentlogistics.persenter.impl.ViewPagerImpl;

/**
 * @Author: a797s
 * @Date: 2020/6/23 12:00
 * @Desc: presenter 管理
 */
public final class PresenterManager {

    private static final PresenterManager ourInstance = new PresenterManager();

    private final ICommonListPresenter mCommonListPresenter;
    private final IMapPresenter mMapPresenter;
    private final ICommonDetailPresenter mDetailPresenter;
    private final ITabLayoutPresenter mTabLayoutPresenter;
    private final IViewPagerPresenter mViewPagerPresenter;
    private final ITokenOrderPresenter mTokenOrderPresenter;

    public static PresenterManager getOurInstance() {
        return ourInstance;
    }

    public ICommonListPresenter getCommonListPresenter() {
        return mCommonListPresenter;
    }

    public IMapPresenter getMapPresenter() {
        return mMapPresenter;
    }

    public ICommonDetailPresenter getDetailPresenter() {
        return mDetailPresenter;
    }

    public ITabLayoutPresenter getTabLayoutPresenter() {
        return mTabLayoutPresenter;
    }

    public IViewPagerPresenter getViewPagerPresenter() {
        return mViewPagerPresenter;
    }

    public ITokenOrderPresenter getTokenOrderPresenter() {
        return mTokenOrderPresenter;
    }

    public PresenterManager() {
        mCommonListPresenter = new CommonListImpl();
        mMapPresenter = new MapImpl();
        mDetailPresenter = new CommonDetailImpl();
        mTabLayoutPresenter = new TabLayoutImpl();
        mViewPagerPresenter = new ViewPagerImpl();
        mTokenOrderPresenter = new TokenOrderImpl();
    }
}
