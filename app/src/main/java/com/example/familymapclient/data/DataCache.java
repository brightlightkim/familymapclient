package com.example.familymapclient.data;

import com.example.familymapclient.fragment.SettingsFragment;
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
import Result.EventResult;
import Result.EventsResult;
import Result.PersonResult;
import Result.PersonsResult;

public class DataCache {
    private static final String EVENT_BOOLEAN_KEY = "boolean";
    private static final String LOGOUT_KEY = "logout";
    private static final String MAP_KEY = "map";
    private static final String SETTING_KEY = "setting";
    private static DataCache instance;

    public synchronized static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {
    }

    private Person user;
    private Event selectedEvent;

    private Set<Person> userPeople;
    private Set<Event> userEvents;

    private Set<Event> selectedEvents;

    private Map<String, Float> eventTypeColor;
    private float[] colors;
    private int colorNum;
    private static final int MIN_COLOR_NUM = 0;
    private static final int MAX_COLOR_NUM = 10;

    private static final String PERSON_ID_KEY = "PERSONID";
    private static final String EVENT_ID_KEY = "EVENTID";

    private Map<String, ArrayList<Event>> personEvent; //Person ID and get an Event by order

    private Map<String, Person> personByID; //Person ID and Find the Person
    private Map<String, Event> eventById; //Event ID and Find the Event

    private Map<String, Person> childByID; //get child by parent's id.

    //For Paternal and Maternal
    private Set<Event> maleEvents; //Person ID and father side
    private Set<Event> femaleEvents; //Person ID and mother side

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

    public Event getEventByEventID(String eventID) {
        return eventById.get(eventID);
    }

    public void setData(String userPersonID, PersonsResult people, EventsResult events) {
        eventTypeColor = new HashMap<>();
        personByID = new HashMap<>();
        eventById = new HashMap<>();
        childByID = new HashMap<>();
        personEvent = new HashMap<>();
        maleEvents = new HashSet<>();
        femaleEvents = new HashSet<>();
        setColors();
        setPeopleData(people);
        user = getPersonByID(userPersonID);
        setEventsData(events);
        selectedEvent = personEvent.get(user.getPersonID()).get(0);
        selectedEvents = userEvents;
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
                    if (compareEvent.getEventType().toLowerCase(Locale.ROOT).equals("birth")) {
                        if (eventList.size() == 1){
                            eventList.add(1, event);
                            break;
                        }
                    }
                    else if (compareEvent.getEventType().toLowerCase(Locale.ROOT).equals("death")){
                        eventList.add(i, event);
                        break;
                    }
                    else{
                        if (compareEvent.getYear() > event.getYear()) {
                            eventList.add(i, event);
                            break;
                        } else if (compareEvent.getYear()==event.getYear()) {
                            if (compareEvent.getEventType().toLowerCase(Locale.ROOT).
                                    compareTo(event.getEventType().toLowerCase(Locale.ROOT))
                                    > 0) {
                                eventList.add(i, event);
                                break;
                            }
                        }
                        if (i == eventList.size()-1){
                            eventList.add(eventList.size(), event);
                            break;
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

    public ArrayList<Person> findPeopleWithText(String text) {
        ArrayList<Person> peopleWithText = new ArrayList<>();
        for (Person person : userPeople) {
            if (person.getFirstName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                    person.getLastName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                peopleWithText.add(person);
            }
        }
        return peopleWithText;
    }

    public ArrayList<Event> findEventsWithText(String text) {
        ArrayList<Event> eventsWithText = new ArrayList<>();
        for (Event event : selectedEvents) {
            if (event.getCountry().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                    event.getCity().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                    event.getEventType().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                    String.valueOf(event.getYear()).contains(text.toLowerCase(Locale.ROOT))) {
                eventsWithText.add(event);
            }
        }
        return eventsWithText;
    }

    public static String getSettingKey() {
        return SETTING_KEY;
    }

    public ArrayList<Event> getLifeEventsByPersonID(String personID) {
        return personEvent.get(personID);
    }

    public static String getLogoutKey() {
        return LOGOUT_KEY;
    }

    public Set<Event> getMaleEvents() {
        return maleEvents;
    }

    public Set<Event> getSelectedEvents() {
        return selectedEvents;
    }

    public void setSelectedEvents(Set<Event> selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    public Set<Event> getFemaleEvents() {
        return femaleEvents;
    }

    public static String getPersonIdKey() {
        return PERSON_ID_KEY;
    }

    public static String getEventIdKey() {
        return EVENT_ID_KEY;
    }

    public static String getEventBooleanKey() {
        return EVENT_BOOLEAN_KEY;
    }

    public static String getMapKey() {
        return MAP_KEY;
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

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Set<Event> getUserEvents() {
        return userEvents;
    }
}
