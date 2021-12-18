package com.example.melotunes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
     EditText login_phoneno,login_password;
     Button Login;
     String Phoneno,Password;
     String PasswordA;
     ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Login Account");
        LoadingBar.setCanceledOnTouchOutside(false);
        login_phoneno=findViewById(R.id.login_phoneno);
        login_password=findViewById(R.id.login_password);
        Login=findViewById(R.id.login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phoneno=login_phoneno.getText().toString();
                Password=login_password.getText().toString();
                Loginfunc(Phoneno,Password);
            }
        });
    }

    private void Loginfunc(String phoneno, String password) {
        //First check if the string is empty or not
        if(TextUtils.isEmpty(phoneno))
        {
            Toast.makeText(this,"Enter your phone number",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Enter your Password",Toast.LENGTH_SHORT).show();
        }
        else{
            LoadingBar.show();
            final DatabaseReference dbReference;
            dbReference= FirebaseDatabase.getInstance().getReference();
            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Check if the phone number given by the user exists in our database or not
                    //if it exists then we will check if the password entered by the user matches the password in the database
                    //which is incorporated with the phone number
                    if(snapshot.child("Users").child(phoneno).exists())
                    {
                        //retrieve the password from database
                        PasswordA=snapshot.child("Users").child(phoneno).child("password").getValue().toString();
                        if(password.equals(PasswordA))
                        {
                            LoadingBar.dismiss();
                            Intent music=new Intent(LoginActivity.this,Music.class);
                            startActivity(music);
                        }
                        else
                        {
                            LoadingBar.dismiss();
                          Toast.makeText(LoginActivity.this,"Please enter valid credentials",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this,"Phone number not registered",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}