package com.example.familymapclient.data;

import com.example.familymapclient.Settings;
import com.example.familymapclient.helperModel.PersonWithRelationship;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;
import Model.User;
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

    private DataCache() {
    }

    private String authToken;

    private Person user;
    private String userID;

    private Set<Person> userPeople;
    private Set<Event> userEvents;

    private Map<String, Float> eventTypeColor;
    private float[] colors;
    private int colorNum;
    private static final int MIN_COLOR_NUM = 0;
    private static final int MAX_COLOR_NUM = 10;

    private Map<String, Set<Person>> people;//Person ID and get Person
    private Map<String, Set<Event>> events; //Event ID and get All Events

    private Map<String, ArrayList<Event>> personEvent; //Person ID and get an Event by order

    private Map<String, Person> personByID; //Person ID and Find the Person
    private Map<String, Event> eventById; //Event ID and Find the Event

    private Map<String, Person> childByID; //get child by parent's id.

    //For Paternal and Maternal
    private Set<Event> maleEvents; //Person ID and father side
    private Set<Event> femaleEvents; //Person ID and mother side

    private Settings settings;

    public ArrayList<PersonWithRelationship> getFamilyWithRelationship(Person person) {
        ArrayList<PersonWithRelationship> familyWithRelationship = new ArrayList<>();
        Person father = getPersonByID(person.getFatherID());
        if (father != null) {
            PersonWithRelationship fatherWithRelationship =
                    new PersonWithRelationship(father, "Father");
            familyWithRelationship.add(fatherWithRelationship);
        }
        Person mother = getPersonByID(person.getMotherID());
        if (mother != null) {
            PersonWithRelationship motherWithRelationship =
                    new PersonWithRelationship(mother, "Mother");
            familyWithRelationship.add(motherWithRelationship);
        }
        Person spouse = getPersonByID(person.getSpouseID());
        if (spouse != null) {
            PersonWithRelationship spouseWithRelationship =
                    new PersonWithRelationship(spouse, "Spouse");
            familyWithRelationship.add(spouseWithRelationship);
        }
        Person child = getChildById(person.getPersonID());
        if (child != null) {
            PersonWithRelationship childWithRelationship =
                    new PersonWithRelationship(child, "Child");
            familyWithRelationship.add(childWithRelationship);
        }
        return familyWithRelationship;
    }

    public Person getChildById(String personID) {
        return childByID.get(personID);
    }

    public Person getPersonByID(String personID) {
        return personByID.get(personID);
    }

    public void setData(String userPersonID, PersonsResult people, EventsResult events) {
        userID = userPersonID;
        eventTypeColor = new HashMap<>();
        personByID = new HashMap<>();
        eventById = new HashMap<>();
        childByID = new HashMap<>();
        personEvent = new HashMap<>();
        maleEvents = new HashSet<>();
        femaleEvents = new HashSet<>();
        setPeopleData(people);
        user = getPersonByID(userPersonID);
        setEventsData(events);
        setColors();
    }

    private void setPeopleData(PersonsResult people) {
        ArrayList<PersonResult> peopleList = people.getData();
        userPeople = new HashSet<>();
        for (PersonResult personResult : peopleList) {
            Person person = new Person(personResult.getPersonID(), personResult.getAssociatedUsername(),
                    personResult.getFirstName(), personResult.getLastName(), personResult.getGender(),
                    personResult.getFatherID(), personResult.getMotherID(), personResult.getSpouseID());
            if (person.getFatherID() != null) {
                childByID.put(person.getFatherID(), person);
            }
            if (person.getMotherID() != null) {
                childByID.put(person.getMotherID(), person);
            }
            userPeople.add(person);
            personByID.put(person.getPersonID(), person);
        }
    }

    private void setEventsData(EventsResult events) {
        ArrayList<EventResult> eventList = events.getData();
        userEvents = new HashSet<>();
        for (EventResult eventResult : eventList) {
            Event event = new Event(eventResult.getEventID(), eventResult.getAssociatedUsername(),
                    eventResult.getPersonID(), eventResult.getLatitude(), eventResult.getLongitude(),
                    eventResult.getCountry(), eventResult.getCity(), eventResult.getEventType(),
                    eventResult.getYear());
            addEventToArrayList(event);
            setEventByGender(event);
            eventById.put(event.getEventID(), event);
            userEvents.add(event);
        }
    }

    private void setEventByGender(Event event) {
        Person person = getPersonByID(event.getPersonID());
        if (person.getGender().toLowerCase(Locale.ROOT).equals("male") ||
                person.getGender().toLowerCase(Locale.ROOT).equals("m")) {
            maleEvents.add(event);
        } else {
            femaleEvents.add(event);
        }
    }

    private void addEventToArrayList(Event event) {
        ArrayList<Event> eventList = personEvent.get(event.getPersonID());
        if (eventList == null) {
            eventList = new ArrayList<>();
            eventList.add(event);
        } else {
            if (event.getEventType().toLowerCase(Locale.ROOT).equals("birth")) {
                eventList.add(0, event);
            } else if (event.getEventType().toLowerCase(Locale.ROOT).equals("death")) {
                eventList.add(eventList.size(), event);
            } else {
                for (int i = 0; i < eventList.size(); i++) {
                    Event compareEvent = eventList.get(i);
                    //If it's not birth or death
                    if (!(compareEvent.getEventType().toLowerCase(Locale.ROOT).equals("birth") ||
                            (compareEvent.getEventType().toLowerCase(Locale.ROOT).equals("death")))) {
                        if (compareEvent.getYear() > event.getYear()) {
                            //If the year is after, we should insert before.
                            eventList.add(i, event);
                            break;
                        } else if (compareEvent.getYear() == event.getYear()) {
                            //alphabetically sort this.
                            if (compareEvent.getEventType().toLowerCase(Locale.ROOT).
                                    compareTo(event.getEventType().toLowerCase(Locale.ROOT))
                                    > 0) {
                                eventList.add(i, event);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //finally replace it with a new order.
        personEvent.put(event.getPersonID(), eventList);
    }

    private void setColors() {
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

    public ArrayList<Event> getLifeEventsByPersonID(String personID) {
        return personEvent.get(personID);
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

    public float[] getColors() {
        return colors;
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

    public Set<Event> getUserEvents() {
        return userEvents;
    }
}
