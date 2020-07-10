package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.citynavigation.CityBean;
import com.rainwood.sentlogistics.model.domain.HotCity;
import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.network.okhttp.HttpResponse;
import com.rainwood.sentlogistics.network.okhttp.OkHttp;
import com.rainwood.sentlogistics.network.okhttp.OnHttpListener;
import com.rainwood.sentlogistics.network.okhttp.RequestParams;
import com.rainwood.sentlogistics.persenter.IMapPresenter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.view.IMapCallback;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 14:36
 * @Desc: 地图相关后台逻辑impl
 */
public final class MapImpl implements IMapPresenter, OnHttpListener {

    private IMapCallback mMapCallback;

    /**
     * 请求城市相关数据
     */
    @Override
    public void requestCityData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URI + "kehuData.php?type=getCitys", params, this);
    }

    @Override
    public void registerViewCallback(IMapCallback callback) {
        mMapCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IMapCallback callback) {
        mMapCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtil.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mMapCallback.onError();
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

        // 获取城市相关数据
        if (result.url().contains("kehuData.php?type=getCitys")) {
            try {
                List<CityBean> cityList = JsonParser.parseJSONArray(CityBean.class, JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("data")).getString("cityAll"));
                List<HotCity> hotCityList = JsonParser.parseJSONArray(HotCity.class, JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("data")).getString("cityHot"));
                mMapCallback.getCityData(cityList, hotCityList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
