package com.example.familymapclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import Request.LoginRequest;
import Result.LoginResult;
import server.ServerProxy;

public class MainActivity extends AppCompatActivity {
    private ServerProxy server;
    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText userPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioButton male;
    private RadioButton female;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverHost = findViewById(R.id.serverHost);
        serverPort = findViewById(R.id.serverPost);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.password);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.emailAddress);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        //this is setting the server with the host and port.
        server = new ServerProxy();
        server.setServerHost(serverHost.getText().toString());
        server.setServerPort(serverPort.getText().toString());

        loginButton.setOnClickListener(v -> {
            //Show the Map Fragment Later.

            //Make a LoginRequest
            String username = userName.getText().toString();
            String password = userPassword.getText().toString();
            LoginRequest request = new LoginRequest(username, password);

            //Now is for the ServerProxy.
            LoginResult result = server.login(request);
            if (result.isSuccess()){
                //Make additional requests to get the data of the user's family and events.
                Toast.makeText(this, firstName.getText().toString() + "," + lastName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
            else {//Fail
                Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}