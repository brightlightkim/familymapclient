package com.example.familymapclient.data;

import android.graphics.Color;

import com.example.familymapclient.Settings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;
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

    //TODO: Events for different colors
    //Let's use the map and add it each time.
    //When one value is used add it with a key and a color
    //If it's not in the map then add the new color and the type.



    private Map<String, Float> eventTypeColor;
    private float[] colors;
    private int colorNum;
    private static final int MIN_COLOR_NUM = 0;
    private static final int MAX_COLOR_NUM = 10;

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
        eventTypeColor = new HashMap<>();
        setPeopleData(people);
        setEventsData(events);
        setColors();
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

    private void setColors(){
        colorNum = 0;
        colors = new float[]{BitmapDescriptorFactory.HUE_CYAN,
        BitmapDescriptorFactory.HUE_AZURE,
        BitmapDescriptorFactory.HUE_BLUE,
        BitmapDescriptorFactory.HUE_GREEN,
        BitmapDescriptorFactory.HUE_MAGENTA,
        BitmapDescriptorFactory.HUE_ORANGE,
        BitmapDescriptorFactory.HUE_RED,
        BitmapDescriptorFactory.HUE_ROSE,
        BitmapDescriptorFactory.HUE_VIOLET,
        BitmapDescriptorFactory.HUE_YELLOW};
    }

    public static int getMaxColorNum() {
        return MAX_COLOR_NUM;
    }

    public static int getMinColorNum() {
        return MIN_COLOR_NUM;
    }

    public Map<String, Float> getEventTypeColor() {
        return eventTypeColor;
    }

    public void setEventTypeColor(Map<String, Float> eventTypeColor) {
        this.eventTypeColor = eventTypeColor;
    }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }

    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    public Person getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public Set<Person> getUserPeople() {
        return userPeople;
    }

    public void setUserPeople(Set<Person> userPeople) {
        this.userPeople = userPeople;
    }

    public Set<Event> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(Set<Event> userEvents) {
        this.userEvents = userEvents;
    }

    public Map<String, Set<Person>> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Set<Person>> people) {
        this.people = people;
    }

    public Map<String, Set<Event>> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Set<Event>> events) {
        this.events = events;
    }

    public Map<String, SortedSet<Event>> getPersonEvent() {
        return personEvent;
    }

    public void setPersonEvent(Map<String, SortedSet<Event>> personEvent) {
        this.personEvent = personEvent;
    }

    public Map<String, Person> getPersonByID() {
        return personByID;
    }

    public void setPersonByID(Map<String, Person> personByID) {
        this.personByID = personByID;
    }

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(Set<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
