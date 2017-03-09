package com.lpf.hasee.okhttp;

import org.json.JSONObject;

/**
 * Created by hasee on 2017/3/8.
 */

public class OkhttpBeans {

    private String msg ;

    private String code ;


    public OkhttpBeans(JSONObject jsonObject) {
        this.msg = jsonObject.optString("msg");
        this.code = jsonObject.optString("code");
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
