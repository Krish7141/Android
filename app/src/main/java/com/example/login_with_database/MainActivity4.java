package com.example.login_with_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity4 extends AppCompatActivity {
    ImageView img;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        img=findViewById(R.id.img);
        img.setImageResource(R.drawable.abcdw);
        swipeRefreshLayout=findViewById(R.id.swipe);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
                        if(activeNetwork!=null){
                            Intent m=new Intent(MainActivity4.this,MainActivity.class);
                            startActivity(m);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
            }
        });
    }
}