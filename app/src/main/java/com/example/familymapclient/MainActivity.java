package com.example.familymapclient;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import Request.LoginRequest;
import Result.LoginResult;
import server.ServerProxy;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String KEY_INDEX = "index";

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

    @SuppressLint("SetTextI18n")
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

        /**
         * This can be changed later.
         * It's for the testing
         */
        serverHost.setText("10.37.11.53");
        serverPort.setText("8080");
        userName.setText("taeyang");
        userPassword.setText("1234");

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        //this is setting the server with the host and port.
        server = new ServerProxy();

        loginButton.setOnClickListener(v -> {
            server.setServerHost(serverHost.getText().toString());
            server.setServerPort(serverPort.getText().toString());

            String username = userName.getText().toString();
            String password = userPassword.getText().toString();
            LoginRequest request = new LoginRequest(username, password);

            LoginResult result = server.login(request);
            if (result == null){
                Toast.makeText(this, "Failed to Connect", Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.isSuccess()){
                    String authToken = result.getAuthtoken();
                    // Make additional requests to get the data of the user's family and events.
                    // server.getPeople(authToken);
                    // server.getEvents(authToken);
                    Toast.makeText(this, firstName.getText().toString() + "," + lastName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else {//Fail
                    Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, getQuizViewModel().getCurrentIndex());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}