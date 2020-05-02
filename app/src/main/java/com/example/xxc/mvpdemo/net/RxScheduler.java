package com.example.xxc.mvpdemo.net;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: RxJava 线程调度
 * @Author: xxc
 * @Date: 2020/4/26 0:09
 * @Version: 1.0
 */
public class RxScheduler {

    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T>FlowableTransformer<T, T> flowableIOMain() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T>ObservableTransformer<T, T> observableIOMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
