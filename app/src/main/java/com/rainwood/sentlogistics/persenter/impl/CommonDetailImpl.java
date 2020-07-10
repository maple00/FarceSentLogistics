package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.network.okhttp.HttpResponse;
import com.rainwood.sentlogistics.network.okhttp.OkHttp;
import com.rainwood.sentlogistics.network.okhttp.OnHttpListener;
import com.rainwood.sentlogistics.network.okhttp.RequestParams;
import com.rainwood.sentlogistics.persenter.ICommonDetailPresenter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.view.ICommonDetailCallback;

import org.json.JSONException;

/**
 * @Author: a797s
 * @Date: 2020/6/23 16:19
 * @Desc: 详情impl(活动详情)
 */
public final class CommonDetailImpl implements ICommonDetailPresenter, OnHttpListener {

    private ICommonDetailCallback mDetailCallback;

    /**
     * 请求活动详情
     *
     * @param activityId
     */
    @Override
    public void requestDetailByAtyId(String activityId) {
        RequestParams params = new RequestParams();
        params.add("acticityId", activityId);
        OkHttp.post(Constants.BASE_URI + "kehuData.php?type=getActivitysById", params, this);
    }

    @Override
    public void registerViewCallback(ICommonDetailCallback callback) {
        mDetailCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ICommonDetailCallback callback) {
        mDetailCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtil.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mDetailCallback.onError();
            return;
        }

       /* try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mMapCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       if (result.url().contains("kehuData.php?type=getActivitysById")){
           try {
               String data = JsonParser.parseJSONObjectString(result.body()).getString("data");
               mDetailCallback.getActivityDetailData(data);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

    }
}
