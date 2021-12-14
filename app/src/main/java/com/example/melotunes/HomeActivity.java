package com.example.melotunes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    LinearLayout ll;
    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ll= findViewById(R.id.ll);
        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
    }
}