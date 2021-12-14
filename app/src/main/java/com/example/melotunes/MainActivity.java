package com.example.melotunes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        Intent i=new Intent(MainActivity.this,HomeActivity.class);
        Thread thread = new Thread()
        {
            @Override
            public void run(){
                try{
                    sleep(5000);//sleep for 5 second
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally{
                    startActivity(i);
                    finish();
                }
            }
        }; thread.start();
    }
} 