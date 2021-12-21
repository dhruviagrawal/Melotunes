package com.example.melotunes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Music extends AppCompatActivity {
    LinearLayout musicll;
    Button offline,online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        musicll=findViewById(R.id.musicll);
        offline=findViewById(R.id.offline);
        online=findViewById(R.id.online);
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent offlinelib=new Intent(Music.this,OfflineLibrary.class);
                startActivity(offlinelib);
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onlinelib=new Intent(Music.this,OnlineLibrary.class);
                startActivity(onlinelib);
            }
        });
    }
}