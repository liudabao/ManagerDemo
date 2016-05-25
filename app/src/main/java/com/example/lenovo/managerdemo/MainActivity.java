package com.example.lenovo.managerdemo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private  void initView() {
        bt1 = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);
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



}
