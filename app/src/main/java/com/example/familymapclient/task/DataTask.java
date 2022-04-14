package com.example.familymapclient.task;

import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.server.ServerProxy;

import Result.EventsResult;
import Result.PersonsResult;

public class DataTask {
    private final String authToken;
    private final ServerProxy server;
    private final DataCache data;
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
