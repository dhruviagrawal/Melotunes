package com.example.melotunes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

     EditText Registerphoneno,Registername,RegisterPassword;
     String phoneno,name,password; //Save Password, Phoneno, Name from EditText
     Button Register;
     ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Registerphoneno=findViewById(R.id.register_phoneno);
        Registername=findViewById(R.id.register_name);
        RegisterPassword=findViewById(R.id.register_password);
        Register=findViewById(R.id.register);
        LoadingBar= new ProgressDialog(this);
        LoadingBar.setTitle("Create Account");
        LoadingBar.setCanceledOnTouchOutside(false);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save the data from edittext in string name,password and phoneno
                phoneno=Registerphoneno.getText().toString();
                name=Registername.getText().toString();
                password=RegisterPassword.getText().toString();
                CreateAccount(phoneno,name,password);

            }
        });
    }

    private void CreateAccount(String phoneno, String name, String password) {
        //First check if the string is empty or not
        if(TextUtils.isEmpty(phoneno))
        {
            Toast.makeText(this,"Enter your phone number",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Enter your Name",Toast.LENGTH_SHORT).show();
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
                    //To check if the phone number already exist in our database
                    if(!snapshot.child("Users").child(phoneno).exists())
                    {
                        //if not exists then create account in database
                        HashMap<String,Object> NewAccount=new HashMap<>();
                        NewAccount.put("phoneno",phoneno);
                        NewAccount.put("name",name);
                        NewAccount.put("password",password);

                        dbReference.child("Users").child(phoneno).updateChildren(NewAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    LoadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        //if exist
                        LoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this,"Phone number already registered",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}