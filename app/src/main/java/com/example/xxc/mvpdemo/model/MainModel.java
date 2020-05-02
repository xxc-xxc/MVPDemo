package com.example.xxc.mvpdemo.model;

import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;
import com.example.xxc.mvpdemo.contract.MainContract;
import com.example.xxc.mvpdemo.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @Description: 负责联网请求
 * @Author: xxc
 * @Date: 2020/4/25 21:20
 * @Version: 1.0
 */
public class MainModel implements MainContract.Model {
    @Override
    public Flowable<BaseObjectBean<UserBean>> login(String username, String password) {
        return RetrofitClient.getInstance().getApi().login(username, password);
    }
}
