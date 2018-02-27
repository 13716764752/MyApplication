package com.simple.raman.okhttptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_get:
                getAsynHttp();
                break;
            case R.id.tv_post:
                postAsynHttp();
                break;
            case R.id.tv_up:
                postAsynFile();
                break;
            case R.id.tv_download:
                downAsynFile();
                break;

            case R.id.tv_jianzhidui:
                httpPost(view);
                break;
            case R.id.tv_Json:
              httpPostJSON(view);
                break;
            case R.id.tv_File:
                try {
                    postFile(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    /**
     * get请求
     */

    private void getAsynHttp() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * post请求
     */
    private void postAsynHttp() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("size", "10")
                .build();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /**
     * 上传
     */

    private void postAsynFile() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        File file = new File("/sdcard/wangshu.txt");
        Request request = new Request.Builder()
                .url("http://192.168.197.1:8080/okHttpTest/okHttpJsonServletTest")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "数据上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 下载
     */
    private void downAsynFile() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //String url="http://http://192.168.245.1:8080/tomcat.png";
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1519725819742&di=beb7a17f56c79526a39f5475475c2501&imgtype=0&src=http%3A%2F%2Fimg3.3lian.com%2F2013%2Fc2%2F8%2Fd%2F64.jpg";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }

                Log.d("wangshu", "文件下载成功");
            }
        });
    }


    /**
     * 上传键值对的方法
     * Android端上传到服务器
     * 自己搭建服务器
     */
    /**
     * post请求
     *
     * @param view
     */
    public void httpPost(View view) {
        //换成自己的ip就行
        String url = "http://192.168.197.1:8080/okHttpTest/okHttpServletTest";
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        FormBody body = new FormBody.Builder()
                .add("name", "张飞+赵云")
                .add("age", "24")
                .build();
        Request request = new Request.Builder().post(body).url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("wangshu", "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("wangshu", "onResponse: " + response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "键值对请求成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 上传Json的方法
     * Android端上传到服务器
     * 自己搭建服务器
     */
    public void httpPostJSON(View view) {
        String json = "{\n" +
                "    \"name\": \"张菲菲 \"\"age\": \"24 \"\n" +
                "}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //换成自己的ip就行
        String url = "http://192.168.197.1:8080/okHttpTest/okHttpJsonServletTest";
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("wangshu", "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("wangshu", "onResponse: " + response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json数据请求成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    /**
     * 上传文件的方法
     * Android端上传到服务器
     * 自己搭建服务器
     */
    /**
     * 上传文件
     *
     * @param view
     * @throws IOException
     */
    public void postFile(View view) throws IOException {
        /**
         * 写数据到文件中,只是模拟下，
         */
      /*  File file = new File(getFilesDir(), "student.txt");
        FileOutputStream fos = openFileOutput(file.getName(),MODE_PRIVATE);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        writer.write("你好啊，我不是程序员！！！");
        writer.close();
        fos.close();*/
        /**
         * 上传文件到服务器中
         */
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient client = new OkHttpClient();//获取OkHttpClient实例
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, new File("/sdcard/wangshu.txt"));
        String uri = "http://192.168.197.1:8080/okHttpTest/okHttpJsonServletTest";
        final Request request = new Request.Builder().url(uri).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("wangshu", "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", "onResponse: " + response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "文件上传请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
