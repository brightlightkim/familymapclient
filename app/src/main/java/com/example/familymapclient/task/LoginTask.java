package com.example.familymapclient.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.server.ServerProxy;

import Request.LoginRequest;
import Result.LoginResult;

public class LoginTask implements Runnable {
    private final Handler messageHandler;
    private LoginRequest loginRequest;
    private final ServerProxy server;
    private final String serverHost;
    private final String serverPort;
    private final static String SUCCESS_KEY = "success";
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";

    private String firstName;
    private String lastName;
    private boolean isSuccess;


    public LoginTask(Handler messageHandler, LoginRequest request, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.loginRequest = request;
        server = new ServerProxy();
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {

        LoginResult result = server.login(loginRequest);

        if (result.isSuccess()) {
            DataTask dataTask = new DataTask(result.getAuthtoken(), serverHost, serverPort);
            dataTask.setData(result.getPersonID());
            firstName = dataTask.getFirstName();
            lastName = dataTask.getLastName();
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        sendMessage();
    }

    private void sendMessage() {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        if (isSuccess) {//When it succeeds send the data.
            messageBundle.putString(FIRST_NAME, firstName);
            messageBundle.putString(LAST_NAME, lastName);
        }
        messageBundle.putBoolean(SUCCESS_KEY, isSuccess);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
