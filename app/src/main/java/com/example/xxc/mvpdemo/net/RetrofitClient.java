package com.example.xxc.mvpdemo.net;

import androidx.annotation.NonNull;

import com.example.xxc.mvpdemo.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static volatile RetrofitClient instance;
    private String baseUrl = "http://www.wanandroid.com/";
    private APIService apiService;

    private RetrofitClient() {
    }

    /**
     * 单例模式创建实例，双重校验锁
     */
    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }

        return instance;
    }

    /**
     * @description: 设置请求头Header
     * @author: xxc
     * @date: 2020/4/25 22:53
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("token", "")
                        .build();
                // 放行
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置拦截器
     * @return
     */
    public Interceptor getInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 设置日志等级
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public APIService getApi() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(getHeaderInterceptor())
                .addInterceptor(getInterceptor())
                .build();

        /*if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(getInterceptor());
        }*/

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                // 设置网络请求的URL地址
                .baseUrl(baseUrl)
                // 设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                // 设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // 创建 网络请求接口 实例
        apiService = retrofit.create(APIService.class);
        return apiService;
    }

}
