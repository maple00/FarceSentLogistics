package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.sentlogistics.model.domain.ActivityList;
import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.network.okhttp.HttpResponse;
import com.rainwood.sentlogistics.network.okhttp.OkHttp;
import com.rainwood.sentlogistics.network.okhttp.OnHttpListener;
import com.rainwood.sentlogistics.network.okhttp.RequestParams;
import com.rainwood.sentlogistics.persenter.ICommonListPresenter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.view.ICommonListCallback;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:55
 * @Desc: 列表页面
 */
public final class CommonListImpl implements ICommonListPresenter, OnHttpListener {

    private ICommonListCallback mCommonListCallback;

    /**
     * 请求活动列表
     */
    @Override
    public void requestEventsListData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URI + "kehuData.php?type=getActivitys", params, this);
    }

    @Override
    public void registerViewCallback(ICommonListCallback callback) {
        mCommonListCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ICommonListCallback callback) {
        mCommonListCallback = null;
    }


    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtil.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mCommonListCallback.onError();
            return;
        }
        /*try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mCommonListCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        // 活动列表
        if (result.url().contains("kehuData.php?type=getActivitys")) {
            try {
                List<ActivityList> activityLists = JsonParser.parseJSONArray(ActivityList.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("data"));
                mCommonListCallback.getActivityList(activityLists);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
