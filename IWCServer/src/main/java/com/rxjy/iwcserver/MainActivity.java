package com.rxjy.iwcserver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        Button finishBtn = (Button) findViewById(R.id.btn_finish);
        finishBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UpdateManager.silentInstall();
            }
        });

        Intent intent = new Intent(this, CoreService.class);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            moveTaskToBack(true);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void install(){
        String apkName = "/data/local/tmp/xxxyyy.apk";
        try{
            Process proc = Runtime.getRuntime().exec("pm install -r "+apkName);
            proc.waitFor();
        }
        catch(Exception e){}
    }


}
