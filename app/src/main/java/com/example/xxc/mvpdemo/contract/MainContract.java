package com.example.xxc.mvpdemo.contract;

import com.example.xxc.mvpdemo.base.BaseView;
import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;

import io.reactivex.Flowable;

/**
 * @Description: MVP模式Demo，Contract是一个联系MVP三方的接口
 * @Author: xxc
 * @Date: 2020/4/24 23:02
 * @Version: 1.0
 */
public interface MainContract {

    /**
     * M：网络请求数据
     */
    interface Model {
        Flowable<BaseObjectBean<UserBean>> login(String username, String password);
    }

    /**
     * V：展示UI
     */
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(BaseObjectBean<UserBean> userBean);
    }

    interface Presenter {
        /**
         * P：逻辑处理
         * @param username
         * @param password
         */
        void login(String username, String password);
    }

}
