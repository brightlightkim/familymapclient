package com.example.familymapclient.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.server.ServerProxy;

import Result.EventsResult;
import Result.PersonsResult;

public class DataTask implements Runnable {
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";
    private final Handler messageHandler;
    private final String authToken;
    private ServerProxy server;
    private String firstName;
    private String lastName;

    public DataTask(Handler messageHandler, String authToken) {
        this.messageHandler = messageHandler;
        this.authToken = authToken;
    }

    @Override
    public void run() {
        server = new ServerProxy();
        PersonsResult personsResult = server.getPeople(authToken);
        EventsResult eventsResult = server.getEvents(authToken);
        //TODO: Now do some data stuff with DataCache.
        //TODO: In the DataCache, add these as parameters.
        //TODO: Then parse those data to whatever our setting will be (ex: map, set, list).
        firstName = "Taeyang";
        lastName = "Kim";
        sendMessage();
    }

    private void sendMessage() {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString(FIRST_NAME, firstName);
        messageBundle.putString(LAST_NAME, lastName);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
