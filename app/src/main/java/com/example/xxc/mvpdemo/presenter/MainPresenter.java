package com.example.xxc.mvpdemo.presenter;

import com.example.xxc.mvpdemo.base.BasePresenter;
import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;
import com.example.xxc.mvpdemo.contract.MainContract;
import com.example.xxc.mvpdemo.model.MainModel;
import com.example.xxc.mvpdemo.net.RxScheduler;

import io.reactivex.functions.Consumer;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private MainContract.Model model;

    public MainPresenter() {
        model = new MainModel();
    }

    @Override
    public void login(String username, String password) {
        // View是否绑定，如果没有绑定，则不执行网络请求
        if (!isViewAttached()) {
            return;
        }

        view.showLoading();
        model.login(username, password)
                .compose(RxScheduler.<BaseObjectBean<UserBean>>flowableIOMain())
                .as(view.<BaseObjectBean<UserBean>>bindAutoDispose())
                .subscribe(new Consumer<BaseObjectBean<UserBean>>() {
                    @Override
                    public void accept(BaseObjectBean<UserBean> userBeanBaseObjectBean) throws Exception {
                        view.onSuccess(userBeanBaseObjectBean);
                        view.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.onError(throwable);
                        view.hideLoading();
                    }
                });
    }
}
