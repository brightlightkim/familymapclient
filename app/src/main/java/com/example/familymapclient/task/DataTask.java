package com.example.familymapclient.task;

import android.os.Handler;

import com.example.familymapclient.server.ServerProxy;

import Result.EventsResult;
import Result.PersonsResult;

public class DataTask implements Runnable{
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
    }
}
