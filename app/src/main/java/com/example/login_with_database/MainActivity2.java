package com.example.login_with_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    Button su,back;
    TextView email,pw,n,cpw;
    ProgressBar psb;
    FirebaseAuth firebaseAuth;
    DatabaseReference reff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        su=findViewById(R.id.su);
        n=findViewById(R.id.n);
        email=findViewById(R.id.email);
        pw=findViewById(R.id.pw);
        cpw=findViewById(R.id.cpw);
        psb=findViewById(R.id.psb);


        back=findViewById(R.id.back);

        firebaseAuth=FirebaseAuth.getInstance();
        final Member information=new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("Member");

        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();



        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork =manager.getActiveNetworkInfo();
        if(activeNetwork==null){
            Toast.makeText(this, "No internet connection...!!!", Toast.LENGTH_SHORT).show();
            Intent n = new Intent(MainActivity2.this, MainActivity4.class);
            startActivity(n);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this,MainActivity.class));
            }
        });

        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information.setN(n.getText().toString().trim());
                information.setPw(pw.getText().toString().trim());
                information.setEm(email.getText().toString().trim());

                final String email1=email.getText().toString().trim();
                final String pw1=pw.getText().toString().trim();
                String cpw1=cpw.getText().toString().trim();
                final String n1=n.getText().toString().trim();

                if(TextUtils.isEmpty(email1)){
                    Toast.makeText(MainActivity2.this,"Please type your Email.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(n1)){
                    Toast.makeText(MainActivity2.this,"Please type your Name.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pw1)){
                    Toast.makeText(MainActivity2.this,"Please type Password.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(cpw1)){
                    Toast.makeText(MainActivity2.this,"Please type Confirm password.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw1.length()<6){
                    Toast.makeText(MainActivity2.this,"Password required at least 6 charactor.",Toast.LENGTH_SHORT).show();
                }
                psb.setVisibility(View.VISIBLE);
                if(pw1.equals(cpw1)){
                    firebaseAuth.createUserWithEmailAndPassword(email1, pw1)
                            .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    psb.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        //reff.push().setValue(information);

                                        FirebaseDatabase.getInstance().getReference("Member")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity2.this, "Verification Email has been send.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("tag","onFailure: Email not send."+e.getMessage());
                                                    }
                                                });
//                                                su.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                Toast.makeText(MainActivity2.this,"Data insert sucessfully.",Toast.LENGTH_SHORT).show();
                                                Toast.makeText(MainActivity2.this,"Please verify your Email.",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(MainActivity2.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity2.this,"Password confirmation doesn't match Password.Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}