package com.example.xxc.mvpdemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.xxc.mvpdemo.R;
import com.example.xxc.mvpdemo.base.BaseMvpActivity;
import com.example.xxc.mvpdemo.bean.BaseObjectBean;
import com.example.xxc.mvpdemo.bean.UserBean;
import com.example.xxc.mvpdemo.contract.MainContract;
import com.example.xxc.mvpdemo.presenter.MainPresenter;
import com.example.xxc.mvpdemo.service.BindImmediateService;
import com.example.xxc.mvpdemo.service.NormalService;
import com.example.xxc.mvpdemo.util.ProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
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
        // (1)map操作符
        /*Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
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
                });*/

        // (2)flatMap操作符
        /*Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("value = " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });*/

        // 4.Flowable&Backpressure
        // 背压（Backpressure）
        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {    //无限循环发事件
                    // 死循环，会导致内存溢出，程序崩溃
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(2000);
                        Log.d(TAG, "" + integer);
                    }
                });*/

        /**
         * 被观察者，上游发射事件
         * BackpressureStrategy.ERROR，背压策略之一，当上下游流速不均匀时直接抛出MissingBackpressureException异常。
         */
        /*Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                // Flowable中切断事件流的方法
//                s.cancel();
                Log.d(TAG, "onSubscribe");
                // 重要方法
                // 1.Flowable设计时采用响应式拉取的方式来解决上下游流速不均匀的问题。
                // 2.调用request方法是告诉上游，下游有多大的事件处理能力，然后上游根据下游的处理能力来决定发送多少事件，这样基本避免出现OOM。
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        flowable.subscribe(subscriber);*/

        // 出现MissingBackpressureException原因：
        // 1.同步代码中（同一线程中）下游没有调用request方法，则上游就认为下游没有处理事件的能力，则抛出MissingBackpressureException
        // 2.Flowable中有一个默认大小为128的事件容器，当上下游在不同的线程时，上游先把事件发送到容器中，当下游调用request方法时，从容器中取出事件发给下游。当容器中的事件超过128，则抛出MissingBackpressureException

        // 5.背压策略
        // (1)使用BackpressureStrategy.BUFFER背压策略，相当于换了一个更大的事件容器，在事件数量达到128时没有OOM。但这并不是说BUFFER策略不会发生OOM，此时的Flowable表现出来的特性与Observable一样，当上游发送过多事件下游处理不过来时，仍会发生OOM。
        // (2)BackpressureStrategy.DROP策略：直接丢弃存不下的事件，即默认存放128个事件，如果再有事件发送过来直接丢弃，等容器中有事件被处理了，才继续存放新的事件
        // (3)BackpressureStrategy.LATEST策略：只保留最新的事件，即只要有新的事件发送过来，就存放，只存放最新的128个事件。

        // 非自己创建的Flowable使用背压策略：
        // interval操作符发送Long类型的事件，从0开始，每隔指定时间把数字+1并发送出来
        /*Flowable.interval(1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

        /*Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    Log.d(TAG, "发射：" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
        /*Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Log.d(TAG, "发射：" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

        // 6.事件处理能力
        // (1)同步
        /*Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "当前处理能力：" + emitter.requested());
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

        // (2)异步
        /*Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Log.d(TAG, "current requested: " + emitter.requested());
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });*/

        // 6.使用RxJavaPlugins做前置处理&后置处理
        /*Maybe.just(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "real onSuccess");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "real onError");
                    }
                });*/

        // 1.Observable
        /*Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "开始发送数据");
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // 订阅方法一
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                // 订阅方法二
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });*/

        // 2.Flowable
        /*Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "开始发送数据");
                for (int i = 0; i < 100; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe: ");
                        // 告诉上游（被观察者）下游（观察者）处理事件的能力
                        s.request(100);
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

        // 3.Single
        /*Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe: ");
                emitter.onSuccess(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/

        // 4.Completable
        /*Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                Log.d(TAG, "subscribe: ");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/

        // 5.Maybe
        /*Maybe.create(new MaybeOnSubscribe<Integer>() {
            @Override
            public void subscribe(MaybeEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe: ");
                emitter.onSuccess(1);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
        // 获取接收流量的总字节数，包含Mobile和WiFi
        long totalRxBytes = TrafficStats.getTotalRxBytes();
        // 获取发送流量的总字节数，包含Mobile和WiFi
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        // 获取接收的总数据包数，包含Mobile和WiFi
        long totalRxPackets = TrafficStats.getTotalRxPackets();
        // 获取发送的总数据包数，包含Mobile和WiFi
        long totalTxPackets = TrafficStats.getTotalTxPackets();

        // 获取Mobile连接接收的总字节数，包含Mobile，不包含WiFi
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        // 获取Mobile发送的字节总数，包含Mobile，不包含WiFi
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        // 获取Mobile接收的数据包总数，包含Mobile，不包含WiFi
        long mobileRxPackets = TrafficStats.getMobileRxPackets();
        // 获取Mobile发送的数据包总数，包含Mobile，不包含WiFi
        long mobileTxPackets = TrafficStats.getMobileTxPackets();

        // 获取指定进程接收流量的总字节数
        long uidRxBytes = TrafficStats.getUidRxBytes(0);
        // 获取指定进程发送流量的总字节数
        long uidTxBytes = TrafficStats.getUidTxBytes(0);
        // 获取指定进程接收流量的总数据包数
        long uidRxPackets = TrafficStats.getUidRxPackets(0);
        // 获取指定进程发送流量的总数据包数
        long uidTxPackets = TrafficStats.getUidTxPackets(0);

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
        /*if (getUsername().isEmpty() || getPassword().isEmpty()) {
            Toast.makeText(this, "帐号密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.login(getUsername(), getPassword());*/

        // 1.启动一个普通服务
//        Intent intent = new Intent(this, NormalService.class);
//        startService(intent);

        // 2.立即绑定服务
//        Intent intent = new Intent(this, BindImmediateService.class);
//        boolean bindFlag = bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // 解绑服务 如果是立即绑定，解绑后自动停止服务
//        unbindService(connection);
//        immediateService = null;

        // 3.延迟绑定服务
//        Intent intent = new Intent(this, BindImmediateService.class);
//        startService(intent); // onCreate() ==> onStartCommand()
        // 绑定服务，如果服务未启动，则先启动服务再绑定
//        bindService(intent, connection, Context.BIND_AUTO_CREATE); // onBind()

        // 解绑服务
//        unbindService(connection); // onUnbind()

        // 重新绑定服务，前提是onUnbind() 返回true(允许再次绑定)
//        bindService(intent, connection, Context.BIND_AUTO_CREATE); // onRebind()

        // 停止服务
//        stopService(intent); // onDestroy()
        startActivity(new Intent(this, ToolbarActivity.class));
    }

    // 声明一个服务对象
    private BindImmediateService immediateService;
    // 创建服务连接对象
    private ServiceConnection connection = new ServiceConnection() {

        // 获取服务对象
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            immediateService = ((BindImmediateService.LocalBinder)iBinder).getService();
        }

        // 与服务断开连接时，将服务对象置为null
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            immediateService = null;
        }
    };


}
