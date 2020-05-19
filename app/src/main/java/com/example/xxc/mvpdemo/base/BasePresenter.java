package com.example.xxc.mvpdemo.base;

public class BasePresenter<V extends BaseView> {

    protected V view;

    /**
     * 绑定view，一般在初始化中调用该方法
     * @param view
     */
    public void attachView(V view) {
        this.view = view;
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */
    public void detachView() {
        this.view = null;
    }

    /**
     * View是否绑定
     * @return
     */
    public boolean isViewAttached() {
        return view != null;
    }

}
