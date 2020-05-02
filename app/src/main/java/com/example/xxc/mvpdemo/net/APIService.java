package com.example.xxc.mvpdemo.net;

import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    /**
     * 登录请求
     * @param username 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Flowable<BaseObjectBean<UserBean>> login(@Field("username") String username, @Field("password") String password);

}
