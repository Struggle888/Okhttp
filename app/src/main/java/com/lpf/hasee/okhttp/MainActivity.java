package com.lpf.hasee.okhttp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static int OKHTTPOK  = 0x01 ;
    private TextView text ;
    OkHttpClient httpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);

    }

    /**
     *  Get 请求
     * @param view
     * @throws IOException
     */
    public void doGet(View view) throws IOException {
//        final Request.Builder builder = new Request.Builder();
//        Request request = builder.get().url("http://1.23xiaochi.com/Home/Content/advert").build();
//        executeRequest(request);

        new OkHttpUtils(MainActivity.this, new OkHttpUtils.Okhttp3CallBack() {
            @Override
            public void Success(JSONObject jsonObject) {
                final OkhttpBeans beans = new OkhttpBeans(jsonObject);
                LogUtils.i(jsonObject.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(beans.getMsg()+"--"+beans.getCode());

                    }
                });
            }
        },"http://1.23xiaochi.com/Home/Content/advert");
    }

    /**
     *  Post 请求
     * @param view
     * @throws IOException
     */
    public void doPost(View view){
        FormBody formBody = new FormBody.Builder()
                .add("status","1")
                .build();
        Request.Builder reBuilder= new Request.Builder();
        Request request = reBuilder.url("http://1.23xiaochi.com/Home/Order/lists").post(formBody).build();
        executeRequest(request);
    }
    /**
     *  Post 请求 上传json
     * @param
     * @throws IOException
     */
    private void doPostString(){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"),"{username:name,password:123}");
        Request.Builder reBuilder= new Request.Builder();
        Request request = reBuilder.url("http://1.23xiaochi.com/Home/Order/lists").post(requestBody).build();
        executeRequest(request);
    }
    /**
     *  Post 请求 上传file
     * @param
     * @throws IOException
     */
    private void doPostFile(){

        File file = new File("");//地址
        if (!file.exists()) {
            LogUtils.i("不存在");
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        Request.Builder reBuilder= new Request.Builder();
        Request request = reBuilder.url("http://1.23xiaochi.com/Home/Order/lists").post(requestBody).build();
        executeRequest(request);
    }



    /**
     *  Post 请求 上传表单  图片加参数
     * @param
     * @throws IOException
     */
    private void doUplode(){

        File file = new File("");//地址
        if (!file.exists()) {
            LogUtils.i("不存在");
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        Request.Builder reBuilder= new Request.Builder();
        Request request =getFileRequest("http://1.23xiaochi.com/Home/Order/lists",null,null);
        executeRequest(request);
    }
    public static Request  getFileRequest(String url,File file,Map<String, String> maps){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(maps==null){
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"),file)
            ).build();
        }else{
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"),file)
            );
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();

    }
    /**
     *  Post 下载文件
     * @param
     * @throws IOException
     */
    private void doDownLoad(){
        final Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url("http://1.23xiaochi.com/Home/Content/advert")
                .build();
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = response.body().byteStream();

                long total = response.body().contentLength();
                int len = 0 ;
                long sum = 0L;
                File file = new File(Environment.getExternalStorageDirectory(),"hyman21.jpg");
                byte[] buf = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);

                while ((len = is.read())!=-1){
                    fos.write(buf,0,len);
                    sum += len ;
                    LogUtils.i("/"+total);
                }

                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    /**
     *  Post 下载文件
     * @param
     * @throws IOException
     */
    private void doDownLoadImg(){
        final Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url("http://1.23xiaochi.com/Home/Content/advert")
                .build();
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = response.body().byteStream();

                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = new ImageView(MainActivity.this);
                        imageView.setImageBitmap(bitmap);
                    }
                });

            }
        });
    }

    private void executeRequest(Request request) {
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                    Message message = new Message();
                    message.what = OKHTTPOK ;
                    Bundle bundle = new Bundle();
                    bundle.putString("json",response.body().string());
                    message.setData(bundle);
                    handler.sendMessage(message);

            }
        });
    }


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

               if (msg.what ==OKHTTPOK){

                   try {
                       OkhttpBeans beans = new OkhttpBeans(new JSONObject(msg.getData().getString("json")));

                       text.setText(beans.getMsg()+"--"+beans.getCode());


                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
