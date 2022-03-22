package com.example.familymapclient.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.server.ServerProxy;

import Request.RegisterRequest;
import Result.RegisterResult;

public class RegisterTask implements Runnable{
    private final Handler messageHandler;
    private RegisterRequest registerRequest;
    private ServerProxy server;
    private String serverHost;
    private String serverPort;
    private final static String SUCCESS_KEY = "success";
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";

    private String firstName;
    private String lastName;
    private boolean isSuccess;

    public RegisterTask(Handler messageHandler, RegisterRequest registerRequest, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.registerRequest = registerRequest;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        server = new ServerProxy();
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
    }


    @Override
    public void run() {
        RegisterResult result = server.register(registerRequest);
        if (result.isSuccess()) {
            DataTask dataTask = new DataTask(result.getAuthtoken(), serverHost, serverPort);
            dataTask.setData();
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
