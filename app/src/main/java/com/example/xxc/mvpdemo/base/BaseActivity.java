package com.example.xxc.mvpdemo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        mActivity = this;
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * 设置监听
     */
    protected void setListener() {

    };

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 获取布局
     * @return
     */
    protected abstract int getLayoutId();
}
