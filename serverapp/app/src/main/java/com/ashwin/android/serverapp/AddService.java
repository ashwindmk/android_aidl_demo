package com.ashwin.android.serverapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class AddService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("aidl-demo", "server: on-create");
    }

    private final IAdd.Stub binder = new IAdd.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            Log.w("aidl-demo", "server: adding " + a + " and " + b);
            return a + b;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.w("aidl-demo", "server: on-unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("aidl-demo", "server: on-destroy");
    }
}
