package com.example.xxc.mvpdemo.contract;

import com.uber.autodispose.AutoDisposeConverter;

public interface IContract {
    interface IModel {

    }

    interface IView {
        /**
         * 显示加载中
         */
        void showLoading();

        /**
         * 隐藏加载
         */
        void hideLoading();

        /**
         * 获取数据失败
         * @param throwable
         */
        void onError(Throwable throwable);

        <T> AutoDisposeConverter<T> bindAutoDispose();
    }

    interface IPresenter {

    }
}
