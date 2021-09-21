package com.example.admin.brios;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Handler handler;
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,UserLogin.class);
                startActivity(intent);
                finish();
            }
        },3000);

        LoginDBManager myDbHelper1 = new LoginDBManager(Splash.this);
        try
        {
            myDbHelper1.createDataBase();
        }
        catch (IOException ioe)
        {
            //throw new Error("Unable to create database");
        }
        myDbHelper1.close();

    }
}
