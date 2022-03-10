package com.example.familymapclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import Request.LoginRequest;
import Result.LoginResult;
import server.ServerProxy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onRadioButtonClicked(View view) {

    }
}