package com.example.melotunes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
     RelativeLayout relLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relLayout= findViewById(R.id.relLayout);
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        Thread thread = new Thread()
        {
            @Override
            public void run(){
                try{
                    sleep(5000);//sleep the splash screen for 5 seconds 
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally{
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
} 