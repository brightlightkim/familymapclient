package com.example.familymapclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Request.LoginRequest;
import Result.LoginResult;
import server.ServerProxy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ServerProxy server = new ServerProxy();
        server.setServerHost("localhost");
        server.setServerPort("8080");
        LoginRequest request = new LoginRequest("taeyang", "1234");
        LoginResult result = server.login(request);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}