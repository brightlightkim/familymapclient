package com.example.familymapclient.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.familymapclient.server.ServerProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;

public class LoginTask implements Runnable{
    private final Handler messageHandler;
    private LoginRequest loginRequest;
    private ServerProxy server;
    private final static String AUTH_KEY = "authorized token"; //With this call the DataTask Function to grab data
    private final static String SUCCESS_KEY = "success";
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";

    private String firstName;
    private String lastName;
    private boolean isSuccess;


    public LoginTask(Handler messageHandler, LoginRequest request) {
        this.messageHandler = messageHandler;
        this.loginRequest = request;
    }

    @Override
    public void run() {
        //Use the com.example.familymapclient.server here. //Server Proxy function here.
        //Add login Function here
        server = new ServerProxy();

        LoginResult result = server.login(loginRequest);
        //I can do below in the data task.
        if (result.isSuccess()){
            //TODO: Check for firstName and lastName value to see if they are not null.
            Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    firstName = bundle.getString(FIRST_NAME);
                    lastName = bundle.getString(LAST_NAME);
                }
            };
            DataTask dataTask = new DataTask(uiThreadMessageHandler, result.getAuthtoken());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(dataTask);
            isSuccess = true;
        }
        else {
            isSuccess = false;
        }
        sendMessage();
    }

    private void sendMessage(){
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        if(isSuccess){//When it succeeds send the data.
            messageBundle.putString(FIRST_NAME, firstName);
            messageBundle.putString(LAST_NAME, lastName);
        }
        messageBundle.putBoolean(SUCCESS_KEY, isSuccess);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
