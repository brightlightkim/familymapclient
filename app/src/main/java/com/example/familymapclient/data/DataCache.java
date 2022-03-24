package com.example.familymapclient.data;

import com.example.familymapclient.Settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import Model.AuthToken;
import Model.Event;
import Model.Person;
import Result.EventResult;
import Result.EventsResult;
import Result.PersonResult;
import Result.PersonsResult;

public class DataCache {
    private static DataCache instance;
    public synchronized static DataCache getInstance() {
        //get another thread works >> not multiple >> don't have
        //thread  >> UI not responsive
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache(){}

    private String authToken;

    private Person user;

    private Set<Person> userPeople;
    private Set<Event> userEvents;

    //Events for different colors
    //Let's use the map and add it each time.

    private Map<String, Set<Person>> people;//Person ID and get Person
    private Map<String, Set<Event>> events; //Event ID and get All Events

    private Map<String, SortedSet<Event>> personEvent; //Person ID and get an Event by order
    private Map<String, Person> personByID; //Event ID and Find the Person

    //For Paternal and Maternal
    private Set<String> paternalAncestors; //Person ID and father side
    private Set<String> maternalAncestors; //Person ID and mother side
    private Settings settings;

    private Person getPersonById(String personID ){
        return null;
    }

    private Event getEventById(String eventID){
        return null;
    }

    private SortedSet<Event> getPersonEvents(String personID){
        return null;
    }

    public void setData(String token, PersonsResult people, EventsResult events){
        authToken = token;
        setPeopleData(people);
        setEventsData(events);
    }

    private void setPeopleData(PersonsResult people) {
        ArrayList<PersonResult> peopleList = people.getData();
        userPeople = new HashSet<>();
        for(PersonResult personResult: peopleList){
            Person person = new Person(personResult.getPersonID(), personResult.getAssociatedUsername(),
                    personResult.getFirstName(), personResult.getLastName(), personResult.getGender(),
                    personResult.getFatherID(), personResult.getMotherID(), personResult.getSpouseID());
            if (person.getSpouseID() == null){
                user = person;
            }
            userPeople.add(person);
        }
    }

    public Person getUser() {
        return user;
    }

    private void setEventsData(EventsResult events) {
        ArrayList<EventResult> eventList = events.getData();
        userEvents = new HashSet<>();
        for(EventResult eventResult: eventList){
            Event event = new Event(eventResult.getEventID(), eventResult.getAssociatedUsername(),
                    eventResult.getPersonID(), eventResult.getLatitude(), eventResult.getLongitude(),
                    eventResult.getCountry(), eventResult.getCity(), eventResult.getEventType(),
                    eventResult.getYear());
            userEvents.add(event);
        }
    }
}
