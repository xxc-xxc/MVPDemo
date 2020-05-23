package com.example.xxc.mvpdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 正常Service
 */
public class NormalService extends Service {

    private static final String TAG = NormalService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "开始普通服务");
        return START_STICKY;
    }

    // 普通服务不存在绑定与解绑流程
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
