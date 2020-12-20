package com.example.login_with_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button si,su,vb;
    TextView email,pw,vt;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        si=findViewById(R.id.si);
        su=findViewById(R.id.su);
        email=findViewById(R.id.email);
        pw=findViewById(R.id.pw);
        vt=findViewById(R.id.vt);
        vb=findViewById(R.id.vb);
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();


        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(i);
                finish();
            }
        });
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1=email.getText().toString().trim();
                String pw1=pw.getText().toString().trim();
                if(TextUtils.isEmpty(email1)){
                    Toast.makeText(MainActivity.this,"Please type your Email.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pw1)){
                    Toast.makeText(MainActivity.this,"Please type Password.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw1.length()<6){
                    Toast.makeText(MainActivity.this,"Password required at least 6 charactor.",Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.signInWithEmailAndPassword(email1, pw1)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (firebaseUser.isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(),MainActivity3.class));
                                        vt.setVisibility(View.GONE);
                                        vb.setVisibility(View.GONE);
                                    }
                                    else{
                                        vt.setVisibility(View.VISIBLE);
                                        vb.setVisibility(View.VISIBLE);
                                        vb.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "Verification Email has been send.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("tag","onFailure: Email not send."+e.getMessage());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                   // startActivity(new Intent(getApplicationContext(),MainActivity3.class));
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"This is wrong Password.Please try again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(activeNetwork!=null){
            if(activeNetwork.getType()== ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "WIFI Enabled", Toast.LENGTH_SHORT).show();
            }
            if(activeNetwork.getType()== ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "MOBILE DATA Enabled", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No internet connection...!!!", Toast.LENGTH_SHORT).show();
            Intent l=new Intent(MainActivity.this,MainActivity4.class);
            startActivity(l);
        }

    }


}