package data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.AuthToken;
import Model.Event;
import Model.Person;

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

    AuthToken authToken;

    Map<String, Set<Person>> people;//Person ID and get Person
    Map<String, Set<Event>> events; //Event ID and get All Events

    Map<String, List<Event>> personEvent; //Person ID and get an Event by order
    Map<String, Person> personByID; //Event ID and Find the Person

    //For Paternal and Maternal
    Set<String> paternalAncestors; //Person ID and father side
    Set<String> maternalAncestors; //Person ID and mother side
    Settings settings;

    Person getPersonById(String personID ){
        return null;
    }

    Event getEventById(String eventID){
        return null;
    }

    List<Event> getPersonEvents(String personID){
        return null;
    }
}
