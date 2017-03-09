package com.lpf.hasee.okhttp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hasee on 2017/3/8.
 */

public class OkHttpUtils {

    Okhttp3CallBack callBack ;

    OkHttpClient httpClient = new OkHttpClient();

    private Context context ;

    public OkHttpUtils(Context context ,Okhttp3CallBack callBack,String path) throws IOException {
        this.callBack = callBack;
        this.context = context;

        doGet(path);
    }

    public  void doGet(String path) throws IOException {
        final Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(path).build();
        executeRequest(request);

    }


    public void doPost(View view){

        FormBody formBody = new FormBody.Builder().add("status","1").build();
        Request.Builder reBuilder= new Request.Builder();
        Request request = reBuilder.url("http://1.23xiaochi.com/Home/Order/lists").post(formBody).build();
        executeRequest(request);

    }

    private void executeRequest(Request request) {
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                handler.sendEmptyMessage(0x02);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    LogUtils.i(jsonObject.toString());
                    callBack.Success(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x01);
                }

            }
        });
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0x01 ){
                Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x02 ){
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public interface Okhttp3CallBack{
         void Success(JSONObject jsonObject);
    }




}
