package com.example.familymapclient.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.server.ServerProxy;

import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class DataTask {
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";
    //private final Handler messageHandler;
    private final String authToken;
    private ServerProxy server;
    private DataCache data;
    private String firstName;
    private String lastName;

    public DataTask(String authToken, String serverHost, String serverPort){
        this.authToken = authToken;
        server = new ServerProxy();
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
        data = DataCache.getInstance();
    }

    public void setData(String userPersonID){
        PersonsResult personsResult = server.getPeople(authToken);
        EventsResult eventsResult = server.getEvents(authToken);
        data.setData(userPersonID, personsResult, eventsResult);
        firstName = data.getUser().getFirstName();
        lastName = data.getUser().getLastName();
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

}
