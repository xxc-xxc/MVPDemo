package com.example.xxc.mvpdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.xxc.mvpdemo.R;
import com.example.xxc.mvpdemo.base.BaseMvpActivity;
import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;
import com.example.xxc.mvpdemo.contract.MainContract;
import com.example.xxc.mvpdemo.presenter.MainPresenter;
import com.example.xxc.mvpdemo.util.ProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.et_username_login)
    TextInputEditText etUsernameLogin;
    @BindView(R.id.et_password_login)
    TextInputEditText etPasswordLogin;
    @BindView(R.id.btn_signin_login)
    Button btnSigninLogin;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ButterKnife.bind(this);
        // 1.基本发送事件&接收事件
        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
//                emitter.onComplete();
                emitter.onError(new Throwable());
            }
        }).subscribe(new Observer<Integer>() {

            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
//                CompositeDisposable compositeDisposable = new CompositeDisposable();
//                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                // 中断事件的传播
                mDisposable.dispose();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

        });*/

        // 2.线程切换
        // (1)默认都在主线程发送接收消息
        /*Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable被观察者线程 = " + Thread.currentThread().getName());
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer观察者线程 = " + Thread.currentThread().getName());
                Log.d(TAG, "accept: " + integer);
            }
        };
        observable.subscribe(consumer);*/

        // (2) 切换线程
        /*Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable被观察者线程 = " + Thread.currentThread().getName());
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer观察者线程 = " + Thread.currentThread().getName());
                Log.d(TAG, "accept: " + integer);
            }
        };

        // 1.多次指定被观察者线程，只有第一次有效；即多次调用subscribeOn()只有第一次有效。
        // 2.观察者线程可以多次指定，即每次调用observeOn()，观察者线程就会被切换一次。
        // 3.RxJava内置的多线程选项：
        //  (1)Schedulers.io()：io线程，通常用于网络，读写文件等IO密集型操作
        //  (2)Schedulers.computation()：CPU计算线程，处理大量计算
        //  (3)Schedulers.newThread()：代表一个普通新线程
        //  (4)AndroidSchedulers.mainThread()：代表Android主线程
        observable
                .subscribeOn(Schedulers.newThread()) // 指定被观察者切换到的线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定观察者切换到的线程
                .subscribe(consumer);*/

        // 3.操作符
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 发射事件
                emitter.onNext(1);
                emitter.onNext(2);
            }
        }).map(new Function<Integer, String>() {
            // 将事件类型由Integer转为String
            @Override
            public String apply(Integer integer) throws Exception {
                return "结果 = " + integer;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });


    }

    @Override
    protected void initView() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 账号
     * @return
     */
    public String getUsername() {
        return etUsernameLogin.getText().toString().trim();
    }

    /**
     * 密码
     * @return
     */
    public String getPassword() {
        return etPasswordLogin.getText().toString().trim();
    }

    @Override
    public void onSuccess(BaseObjectBean<UserBean> userBean) {
        Toast.makeText(mContext, userBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        ProgressDialog.getInstance().show(mContext);
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance().dismiss();
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick(R.id.btn_signin_login)
    public void onViewClicked() {
        if (getUsername().isEmpty() || getPassword().isEmpty()) {
            Toast.makeText(this, "帐号密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.login(getUsername(), getPassword());
    }
}
