package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.sentlogistics.model.domain.PublishType;
import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.network.okhttp.HttpResponse;
import com.rainwood.sentlogistics.network.okhttp.OkHttp;
import com.rainwood.sentlogistics.network.okhttp.OnHttpListener;
import com.rainwood.sentlogistics.network.okhttp.RequestParams;
import com.rainwood.sentlogistics.persenter.ITokenOrderPresenter;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.view.ITokenOrderCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:34
 * @Desc: 下单
 */
public final class TokenOrderImpl implements ITokenOrderPresenter {

    private ITokenOrderCallback mTokenOrderCallback;

    /**
     * 请求订单发布类型
     */
    @Override
    public void requestOrderPublishType() {
        RequestParams params = new RequestParams();
        OkHttp.setCache(true);
        OkHttp.post(Constants.BASE_URI + "kehuData.php?type=getOrderType", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                mTokenOrderCallback.onError();
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                LogUtil.d("sxs", "--- result --" + result.body());
                try {
                    String code = JsonParser.parseJSONObjectString(result.body()).getString("code");
                    if (!"1".equals(code)) {
                        mTokenOrderCallback.onError();
                    } else {
                        List<String> data = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(result.body()).getString("data"));
                        List<PublishType> typeList = new ArrayList<>();
                        for (String datum : data) {
                            PublishType type = new PublishType();
                            type.setType(datum);
                            typeList.add(type);
                        }
                        mTokenOrderCallback.getPublishTypeData(typeList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(ITokenOrderCallback callback) {
        mTokenOrderCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ITokenOrderCallback callback) {
        mTokenOrderCallback = null;
    }


}
