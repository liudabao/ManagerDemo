package com.example.lenovo.managerdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/5/25.
 */
public class WindowService extends Service {

    WindowManager manager;
    WindowManager.LayoutParams params;
    MyView myView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private  void initView(){
        manager=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params=new WindowManager.LayoutParams();
        params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format= PixelFormat.TRANSLUCENT;
        params.width=250;
        params.height=250;
        params.x=0;
        params.y=0;
        myView=new MyView(this);

    }

    @Override
    public void onCreate(){
        initView();
        super.onCreate();
        Log.e("service","start");
    }
    @Override
    public int onStartCommand(Intent intent, int flag, int startId){

        if(Build.VERSION.SDK_INT>=23){
            if(Settings.canDrawOverlays(getApplicationContext())){
                Log.e("service","1");
                manager.addView(myView, params);
            }
            else {
                Intent intent1=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent1);
            }
        }
        else {
            Log.e("service","2");
            manager.addView(myView, params);
        }

        return super.onStartCommand(intent, flag, startId);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }



}
