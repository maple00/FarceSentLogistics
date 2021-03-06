package com.rainwood.sentlogistics.network.okhttp;

import android.util.Log;

import com.rainwood.sentlogistics.base.BaseApplication;
import com.rainwood.sentlogistics.network.app.DataStorage;
import com.rainwood.sentlogistics.network.json.JsonParser;
import com.rainwood.sentlogistics.utils.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Relin
 * on 2018-09-25.
 */
public class OkCookie implements Serializable, CookieJar {


    private List<Map<String, String>> cookieList = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        saveCookie(httpUrl, list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return loadCookie(httpUrl);
    }

    /**
     * 加载Cookie
     *
     * @param httpUrl http请求数据
     * @param list    cookie数据
     */
    private void saveCookie(HttpUrl httpUrl, List<Cookie> list) {
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            Cookie cookie = list.get(i);
            Map<String, String> cookieMap = new HashMap<>();
            cookieMap.put("host", httpUrl.host());
            cookieMap.put("name", cookie.name());
            cookieMap.put("value", cookie.value());
            cookieMap.put("expiresAt", String.valueOf(cookie.expiresAt()));
            cookieMap.put("domain", cookie.domain());
            cookieMap.put("path", cookie.path());
            cookieMap.put("secure", cookie.secure() ? "1" : "0");
            cookieMap.put("httpOnly", cookie.httpOnly() ? "1" : "0");
            cookieMap.put("hostOnly", cookie.hostOnly() ? "1" : "0");
            cookieMap.put("persistent", cookie.persistent() ? "1" : "0");
            cookieList.add(cookieMap);
        }
        String cookieJson = JsonParser.parseMapList(cookieList);
        Log.i(this.getClass().getSimpleName(), cookieJson);
        DataStorage.with(BaseApplication.app).put("ok" + httpUrl.host(), cookieJson);
    }

    /**
     * 保存Cookie
     *
     * @param httpUrl http请求数据
     * @return 缓存的Cookie数据
     */
    private List<Cookie> loadCookie(HttpUrl httpUrl) {
        String requestHost = httpUrl.host();
        String cache = DataStorage.with(BaseApplication.app).getString("ok" + requestHost, "[]");
        List<Map<String, String>> list = JsonParser.parseJSONArray(cache);
        List<Cookie> cookieList = new ArrayList<>();
        for (int i = 0; i < ListUtil.getSize(list); i++) {
            Map<String, String> cookieMap = list.get(i);
            String host = cookieMap.get("host");
            String name = cookieMap.get("name");
            String value = cookieMap.get("value");
            String domain = cookieMap.get("domain");
            String path = cookieMap.get("path");
            if (cookieMap.get("expiresAt") != null) {
                long expiresAt = Long.parseLong(cookieMap.get("expiresAt"));
                if (expiresAt > System.currentTimeMillis() && requestHost.equals(host)) {
                    Cookie cookie = new Cookie.Builder()
                            .name(name)
                            .value(value)
                            .expiresAt(expiresAt)
                            .domain(domain)
                            .path(path)
                            .secure()
                            .httpOnly()
                            .build();
                    cookieList.add(cookie);
                }
            }
        }
        if (ListUtil.getSize(cookieList) == 0) {
            saveCookie(httpUrl, cookieList);
        }
        return cookieList;
    }

    /**
     * 获取Cookie数据
     *
     * @return
     */
    public static List<Cookie> getCookie(String hostKey) {
        String cache = DataStorage.with(BaseApplication.app).getString("ok" + hostKey, "[]");
        List<Map<String, String>> list = JsonParser.parseJSONArray(cache);
        List<Cookie> cookieList = new ArrayList<>();
        for (int i = 0; i < ListUtil.getSize(list); i++) {
            Map<String, String> cookieMap = list.get(i);
            String host = cookieMap.get("host");
            String name = cookieMap.get("name");
            String value = cookieMap.get("value");
            long expiresAt = Long.parseLong(cookieMap.get("expiresAt"));
            String domain = cookieMap.get("domain");
            String path = cookieMap.get("path");
            if (expiresAt > System.currentTimeMillis()) {
                Cookie cookie = new Cookie.Builder()
                        .name(name)
                        .value(value)
                        .expiresAt(expiresAt)
                        .domain(domain)
                        .path(path)
                        .secure()
                        .httpOnly()
                        .build();
                cookieList.add(cookie);
            }
        }
        return cookieList;
    }

    /**
     * 删除Cookie缓存
     *
     * @param host
     */
    public static void remove(String host) {
        DataStorage.with(BaseApplication.app).put("ok" + host, "[]");
    }

}
