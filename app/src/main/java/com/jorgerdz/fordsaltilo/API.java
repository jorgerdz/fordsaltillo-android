package com.jorgerdz.fordsaltilo;
import android.content.Context;

import com.loopj.android.http.*;


/**
 * Created by jorgerdz on 7/3/15.
 */
public class API {
    private static final String BASE_URL = "http://192.168.0.109:3000/";

    protected static AsyncHttpClient client = new AsyncHttpClient();
    protected static PersistentCookieStore storage;

    public static void setStorage(Context context){
        storage = new PersistentCookieStore(context);
        client.setCookieStore(storage);
    }

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

