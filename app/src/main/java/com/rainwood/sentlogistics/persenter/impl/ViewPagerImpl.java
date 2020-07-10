package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.sentlogistics.model.domain.BulletinData;
import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.network.okhttp.HttpResponse;
import com.rainwood.sentlogistics.network.okhttp.OkHttp;
import com.rainwood.sentlogistics.network.okhttp.OnHttpListener;
import com.rainwood.sentlogistics.network.okhttp.RequestParams;
import com.rainwood.sentlogistics.persenter.IViewPagerPresenter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.view.IViewPagerCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:58
 * @Desc: viewPager impl
 */
public final class ViewPagerImpl implements IViewPagerPresenter {

    private List<IViewPagerCallbacks> mCallbacksList = new ArrayList<>();

    @Override
    public void requestContentByType(String type) {
        for(IViewPagerCallbacks callback : mCallbacksList) {
            if(callback.getBulletinType().equals(type)) {
                callback.onLoading();
            }
        }
        // 获取消息公告列表
        RequestParams params = new RequestParams();
        params.add("type", type);
        params.add("status", "货主端");
        OkHttp.post(Constants.BASE_URI + "kehuData.php?type=newsBulletin", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                handleNetworkError(type);
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                LogUtil.d("sxs", "-- 消息通告 -- " + result.body());
                try {
                    List<BulletinData> bulletinList = JsonParser.parseJSONArray(BulletinData.class,
                            JsonParser.parseJSONObjectString(result.body()).getString("data"));
                    for (IViewPagerCallbacks pagerCallbacks : mCallbacksList) {
                        if (pagerCallbacks.getBulletinType().equals(type)){
                            if (ListUtil.getSize(bulletinList) == 0){
                                pagerCallbacks.onEmpty();
                            }else {
                                pagerCallbacks.getBulletinData(bulletinList);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void handleNetworkError(String type) {
        for(IViewPagerCallbacks callback : mCallbacksList) {
            if(callback.getBulletinType().equals(type)) {
                callback.onError();
            }
        }
    }

    @Override
    public void registerViewCallback(IViewPagerCallbacks callback) {
        if(!mCallbacksList.contains(callback)) {
            mCallbacksList.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IViewPagerCallbacks callback) {
        mCallbacksList.remove(callback);
    }
}
