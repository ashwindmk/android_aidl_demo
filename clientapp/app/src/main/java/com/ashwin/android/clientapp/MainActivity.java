package com.ashwin.android.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ashwin.android.serverapp.IAdd;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection serverConnection;
    private IAdd addService;

    private Intent serverIntent;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("aidl-demo", "client: on-create");

        serverIntent = new Intent("com.ashwin.android.serverapp.server");
        serverIntent.setPackage("com.ashwin.android.serverapp");
    }

    public void bind(View view) {
        if (addService == null) {
            serverConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d("aidl-demo", "client: service connected");
                    addService = IAdd.Stub.asInterface((IBinder) iBinder);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.d("aidl-demo", "client: service disconnected");
                    addService = null;
                }
            };
        }

        bindService(serverIntent, serverConnection, Context.BIND_AUTO_CREATE);
    }

    public void test(View view) {
        if (addService != null) {
            try {
                int a = random.nextInt(11);
                int b = random.nextInt(11);
                int result = addService.add(a, b);
                Log.w("aidl-demo", "client: result: " + result);
            } catch (RemoteException e) {
                Log.e("aidl-demo", "Exception while calling add method", e);
            }
        } else {
            Log.w("aidl-demo", "client: service is not bound");
            Toast.makeText(this, "Service is not bound", Toast.LENGTH_LONG).show();
        }
    }

    public void unbind(View view) {
        if (addService != null) {
            unbindService(serverConnection);
            addService = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbind(null);
    }
}
