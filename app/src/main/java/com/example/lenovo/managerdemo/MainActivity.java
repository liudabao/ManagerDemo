package com.example.lenovo.managerdemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Debug;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt7;
    Button bt8;
    MyView myView;
    WindowService.MyBinder binder;
    WindowManager manager;
    WindowManager.LayoutParams params;
    int viewX;
    int viewY;

    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder=(WindowService.MyBinder)service;
            binder.init();
            myView=binder.getView();
            params=binder.getLayout();
            myView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x=(int)event.getRawX();
                    int y=(int)event.getRawY();
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            viewX=(int)event.getX();
                            viewY=(int)event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            params.x=x-viewX;
                            params.y=y-viewY;
                            manager.updateViewLayout(myView, params);
                            //scrollBy(viewX-x, viewY-y);
                            //viewX=x;
                            // viewY=y;
                            break;
                        case MotionEvent.ACTION_UP:
                            params.x=x-viewX;
                            params.y=y-viewY;
                            manager.updateViewLayout(myView, params);
                            break;
                    }
                    return true;
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.e("service", "unbind");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        initView();
    }

    private  void initView() {
        bt1 = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);
        bt5 = (Button) findViewById(R.id.button5);
        bt6 = (Button) findViewById(R.id.button6);
        bt7 = (Button) findViewById(R.id.button7);
        bt8 = (Button) findViewById(R.id.button8);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageInfo();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVersion();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                install();
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalMemory();
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processMemory();
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity();
                getService();
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView();
            }
        });

    }

    private void packageInfo(){
        PackageManager manager=getPackageManager();
        List<ApplicationInfo> app=manager.getInstalledApplications(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        for(ApplicationInfo info:app){
            Log.e("app",""+info.loadLabel(manager)+" "+info.packageName+" "+info.flags);

        }
    }

    private void getVersion(){
        PackageManager manager=getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(getPackageName(), 0);
            Log.e("version", info.versionName+" "+info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void install(){
        File file=new File("/sdcard/Download","app.apk");
        if(!file.exists()){
            Log.e("apk","not exist");
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://"+file.toString()),"application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void totalMemory(){
        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memory=new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memory);
        long size=memory.availMem;
        String memorySize= Formatter.formatFileSize(MainActivity.this, size);
        Log.e("Total Memory", memorySize);
    }

    private void processMemory(){
        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList=manager.getRunningAppProcesses();
        Log.e("Process number"," "+processInfoList.size());
        for(int i=0;i<processInfoList.size();i++){
            ActivityManager.RunningAppProcessInfo processInfo=processInfoList.get(i);
            int pid=processInfo.pid;
            int uid=processInfo.uid;
            String processName=processInfo.processName;
            int [] memoryId=new int[]{pid};
            Debug.MemoryInfo[] memoryInfo=manager.getProcessMemoryInfo(memoryId);
            int processSize=memoryInfo[0].getTotalPss();
            Log.e("Process","pid "+pid+" ProcessName: "+processName);
            Log.e("Process","uid "+uid+" ProcessSize: "+processSize+" kb");
        }
    }


    private void getActivity(){
        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos=manager.getRunningTasks(1000);
        for(ActivityManager.RunningTaskInfo taskInfo: taskInfos){
            Log.e("Activity",taskInfo.baseActivity.getClassName());
        }
    }

    private void getService(){
        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos=manager.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo serviceInfo: serviceInfos){
            Log.e("Service",serviceInfo.service.getClassName());
        }
    }

    private void addView(){
        Intent intent=new Intent(MainActivity.this, WindowService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
       // startService(intent);
    }

    private void removeView(){
        Intent intent=new Intent(MainActivity.this, WindowService.class);
        unbindService(connection);
       // stopService(intent);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                viewX=x;
                viewY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                params.x=x-viewX;
                params.y=y-viewY;
                manager.updateViewLayout(myView, params);
                //scrollBy(viewX-x, viewY-y);
                //viewX=x;
                // viewY=y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
