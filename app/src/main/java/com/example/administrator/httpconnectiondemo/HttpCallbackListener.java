package com.example.administrator.httpconnectiondemo;

/**
 * Created by Administrator on 2017-5-17.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
