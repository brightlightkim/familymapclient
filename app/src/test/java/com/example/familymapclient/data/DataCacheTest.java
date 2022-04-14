package com.example.familymapclient.data;

import com.example.familymapclient.helperModel.PersonWithRelationship;
import com.example.familymapclient.server.ServerProxy;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.EventResult;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.PersonsResult;

/**
 * This test is after the load of the given data of Sheila Parker
 * This test is for the public functions of DataCache and test the helper functions
 * by its variable counts
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataCacheTest {
    private static DataCache data;
    private static Setting setting;
    private static LoginResult loginResult;
    private static PersonsResult personsResult;
    private static EventsResult eventsResult;
    private static int userPeopleSize;
    private static int userEventsSize;
    private static int eventTypeColorSize;
    private static int personEventSize;
    private static int personByIdSize;
    private static int eventByIdSize;
    private static int childByIDSize;
    private static int maleEventsSize;
    private static int femaleEventsSize;
    private static Person sheilaParker;
    private static Event frogEvent;
    private static Person blaineMcGary;
    private static Person notExistPerson;

    @BeforeClass
    public static void setUp() {
        ServerProxy server = ServerProxy.initialize();
        String serverHost = "localhost";
        String serverPort = "8080";
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
        String sampleUsername = "sheila";
        String samplePassword = "parker";
        LoginRequest loginRequest = new LoginRequest(sampleUsername, samplePassword);
        loginResult = server.login(loginRequest);
        personsResult = server.getPeople(loginResult.getAuthtoken());
        eventsResult = server.getEvents(loginResult.getAuthtoken());

        data = DataCache.getInstance();
        setting = Setting.getInstance();
        notExistPerson = new Person("not exist", "me",
                "tae", "yang", "male", "notExistDad",
                "notExistMom", "notExistSpouse");
        userPeopleSize = 8;
        userEventsSize = 16;
        eventTypeColorSize = 0;
        personEventSize = 8;
        personByIdSize = 8;
        eventByIdSize = 16;
        childByIDSize = 6;
        maleEventsSize = 6;
        femaleEventsSize = 10;

    }

    @Test
    public void Test1_1_setDateFailed() {
        data.setData(null, null, null);
        assertNull(data.getUser());
        assertNull(data.getUser());
        assertNull(data.getSelectedEvent());
        assertNull(data.getUserPeople());
        assertNull(data.getUserEvents());
        assertNull(data.getSelectedEvents());
        assertNull(data.getEventTypeColor());
        assertNull(data.getColors());
        assertNull(data.getPersonEvent());
        assertNull(data.getEventById());
        assertNull(data.getChildByID());
        assertNull(data.getMaleEvents());
        assertNull(data.getFemaleEvents());
    }

    @Test
    public void Test1_2_setDataSuccess() {
        data.setData(loginResult.getPersonID(), personsResult, eventsResult);
        assertNotNull(data.getEventTypeColor());
        assertEquals(eventTypeColorSize, data.getEventTypeColor().size());
        assertNotNull(data.getPersonByID());
        assertEquals(personByIdSize, data.getPersonByID().size());
        assertNotNull(data.getEventById());
        assertEquals(eventByIdSize, data.getEventById().size());
        assertNotNull(data.getChildByID());
        assertEquals(childByIDSize, data.getChildByID().size());
        assertNotNull(data.getPersonEvent());
        assertEquals(personEventSize, data.getPersonEvent().size());
        assertNotNull(data.getMaleEvents());
        assertEquals(maleEventsSize, data.getMaleEvents().size());
        assertNotNull(data.getFemaleEvents());
        assertEquals(femaleEventsSize, data.getFemaleEvents().size());
        assertNotNull(data.getUserPeople());
        assertEquals(userPeopleSize, data.getUserPeople().size());
        assertNotNull(data.getUserEvents());
        assertEquals(userEventsSize, data.getUserEvents().size());
        assertNotNull(data.getUser());
        assertNotNull(data.getSelectedEvent());
        assertNotNull(data.getSelectedEvents());
        assertNotNull(data.getColors());
        sheilaParker = data.getUser();
        frogEvent = data.getEventByEventID("Jones_Frog");
        blaineMcGary = data.getPersonByID("Blaine_McGary");
    }

    @Test
    public void Test2_1_findPeopleWithTextSuccess() {
        assertEquals(5, data.findPeopleWithText("s").size());
        assertEquals(1, data.findPeopleWithText("sh").size());
        assertEquals(sheilaParker.getPersonID(),
                data.findPeopleWithText("sh").get(0).getPersonID());
        assertEquals(sheilaParker.getAssociatedUsername(),
                data.findPeopleWithText("sh").get(0).getAssociatedUsername());
        assertEquals(sheilaParker.getFirstName(),
                data.findPeopleWithText("sh").get(0).getFirstName());
        assertEquals(sheilaParker.getLastName(),
                data.findPeopleWithText("sh").get(0).getLastName());
        assertEquals(sheilaParker.getFatherID(),
                data.findPeopleWithText("sh").get(0).getFatherID());
    }

    @Test
    public void Test2_2_findPeopleWithTextFailed() {
        assertNotNull(data.findPeopleWithText("greg"));
        assertNotNull(data.findPeopleWithText("sha"));
        assertEquals(0, data.findPeopleWithText("greg").size());
        assertEquals(0, data.findPeopleWithText("sha").size());
    }

    @Test
    public void Test3_1_findEventsWithTextSucceed() {
        assertEquals(2, data.findEventsWithText("fr").size());
        assertEquals(1, data.findEventsWithText("frog").size());
        assertEquals(frogEvent.getEventID(),
                data.findEventsWithText("frog").get(0).getEventID());
        assertEquals(frogEvent.getEventType(),
                data.findEventsWithText("frog").get(0).getEventType());
        assertEquals(frogEvent.getAssociatedUsername(),
                data.findEventsWithText("frog").get(0).getAssociatedUsername());
        assertEquals(frogEvent.getPersonID(),
                data.findEventsWithText("frog").get(0).getPersonID());
        assertEquals(frogEvent.getCity(),
                data.findEventsWithText("frog").get(0).getCity());
        assertEquals(frogEvent.getCountry(),
                data.findEventsWithText("frog").get(0).getCountry());
    }

    @Test
    public void Test3_2_findEventsWithTextFailed() {
        assertNotNull(data.findEventsWithText("greg"));
        assertNotNull(data.findEventsWithText("sha"));
        assertEquals(0, data.findEventsWithText("greg").size());
        assertEquals(0, data.findEventsWithText("sha").size());
    }

    @Test
    public void Test4_1_getFamilyWithRelationshipSucceed() {
        ArrayList<PersonWithRelationship> sheilaFamily = data.getFamilyWithRelationship(sheilaParker);
        assertNotNull(sheilaFamily);
        assertEquals(3, sheilaFamily.size());
        assertEquals("Father", sheilaFamily.get(0).getRelationship());
        assertEquals("Mother", sheilaFamily.get(1).getRelationship());
        assertEquals("Spouse", sheilaFamily.get(2).getRelationship());

        ArrayList<PersonWithRelationship> blaineFamily = data.getFamilyWithRelationship(blaineMcGary);
        assertNotNull(blaineFamily);
        assertEquals(4, blaineFamily.size());
        assertEquals("Father", blaineFamily.get(0).getRelationship());
        assertEquals("Mother", blaineFamily.get(1).getRelationship());
        assertEquals("Spouse", blaineFamily.get(2).getRelationship());
        assertEquals("Child", blaineFamily.get(3).getRelationship());
    }

    @Test
    public void Test4_2_getFamilyWithRelationshipFailed() {
        ArrayList<PersonWithRelationship> nonExistFamily = data.getFamilyWithRelationship(notExistPerson);
        assertNotNull(nonExistFamily);
        assertEquals(0, nonExistFamily.size());
    }

    /**
     * I implemented this sorting by change in the MapFragment
     * So I will mock how I changed it here and test it base on that
     */
    @Test
    public void Test5_1_filterEventsBySettingSucceed() {
        Set<Event> selectedEvents = new HashSet<>();
        setting.setMaleEventsOn(true);
        setting.setFemaleEventsOn(true);
        if (setting.isMaleEventsOn() && setting.isFemaleEventsOn()) {
            selectedEvents.addAll(data.getMaleEvents());
            selectedEvents.addAll(data.getFemaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(data.getSelectedEvents());
        assertEquals(userEventsSize, data.getSelectedEvents().size());
        selectedEvents.clear();
        setting.setMaleEventsOn(true);
        setting.setFemaleEventsOn(false);
        if (setting.isMaleEventsOn() && !setting.isFemaleEventsOn()) {
            selectedEvents.addAll(data.getMaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(data.getSelectedEvents());
        assertEquals(maleEventsSize, data.getSelectedEvents().size());
        selectedEvents.clear();
        setting.setMaleEventsOn(false);
        setting.setFemaleEventsOn(true);
        if (setting.isFemaleEventsOn() && !setting.isMaleEventsOn()) {
            selectedEvents.addAll(data.getFemaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(selectedEvents);
        assertEquals(femaleEventsSize, selectedEvents.size());
        selectedEvents.clear();
        setting.setMaleEventsOn(false);
        setting.setFemaleEventsOn(false);
        if (!(setting.isMaleEventsOn() && setting.isFemaleEventsOn())) {
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(selectedEvents);
        assertEquals(0, selectedEvents.size());
        selectedEvents.clear();
        data.setSelectedEvents(data.getUserEvents());
    }

    @Test
    public void Test5_2_filterEventsBySettingFailed(){
        Set<Event> selectedEvents = new HashSet<>();
        setting.setMaleEventsOn(true);
        setting.setFemaleEventsOn(true);
        if (setting.isMaleEventsOn() && setting.isFemaleEventsOn()) {
            selectedEvents.addAll(data.getFemaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(data.getSelectedEvents());
        assertNotEquals(userEventsSize, data.getSelectedEvents().size());
        selectedEvents.clear();
        setting.setMaleEventsOn(true);
        setting.setFemaleEventsOn(false);
        if (setting.isMaleEventsOn() && !setting.isFemaleEventsOn()) {
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(data.getSelectedEvents());
        assertNotEquals(maleEventsSize, data.getSelectedEvents().size());
        selectedEvents.clear();
        setting.setMaleEventsOn(false);
        setting.setFemaleEventsOn(true);
        if (setting.isFemaleEventsOn() && !setting.isMaleEventsOn()) {
            selectedEvents.addAll(data.getMaleEvents());
            selectedEvents.addAll(data.getFemaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(selectedEvents);
        assertNotEquals(femaleEventsSize, selectedEvents.size());
        selectedEvents.clear();
        setting.setMaleEventsOn(false);
        setting.setFemaleEventsOn(false);
        if (!(setting.isMaleEventsOn() && setting.isFemaleEventsOn())) {
            selectedEvents.addAll(data.getMaleEvents());
            data.setSelectedEvents(selectedEvents);
        }
        assertNotNull(selectedEvents);
        assertNotEquals(0, selectedEvents.size());
        selectedEvents.clear();
        data.setSelectedEvents(data.getUserEvents());
    }

    /**
     * I implemented this function in DataCache as a private function.
     * So I just used the result from setData() function and check it worked.
     */
    @Test
    public void Test6_1_sortPersonIndividualEventsSucceed() {
        ArrayList<Event> sheilaLifeEvents = data.getLifeEventsByPersonID(sheilaParker.getPersonID());
        assertNotNull(sheilaLifeEvents);
        assertEquals(5, sheilaLifeEvents.size());
        assertEquals("birth", sheilaLifeEvents.get(0).getEventType().toLowerCase(Locale.ROOT));
        assertEquals("death", sheilaLifeEvents.get(sheilaLifeEvents.size()-1).getEventType().toLowerCase(Locale.ROOT));
        assertEquals(2012, sheilaLifeEvents.get(1).getYear());
        assertEquals(2014, sheilaLifeEvents.get(2).getYear());
    }

    @Test
    public void Test6_2_sortPersonIndividualEventsSucceed() {
        ArrayList<Event> nonExistPersonLifeList = data.getLifeEventsByPersonID(notExistPerson.getPersonID());
        assertNull(nonExistPersonLifeList);

        Person unlikelyPerson = new Person("unlikely", "hi", "tae",
                "yang", "m", null, null, null);

        EventResult sameEvent1 = new EventResult(true, "hi", "no", unlikelyPerson.getPersonID(),
                11.2f, 11.3f, "Korea", "Provo", "hi", 2022);
        EventResult sameEvent2 = new EventResult(true, "maybe", "hia", unlikelyPerson.getPersonID(),
                11.2f, 11.3f, "Korea", "Provo", "hi", 2022);

        PersonResult unlikelyPersonResult = new PersonResult(unlikelyPerson.getAssociatedUsername(),
                unlikelyPerson.getPersonID(), unlikelyPerson.getFirstName(), unlikelyPerson.getLastName(),
                unlikelyPerson.getGender(), unlikelyPerson.getFatherID(), unlikelyPerson.getMotherID(),
                unlikelyPerson.getSpouseID(), true);
        personsResult.getData().add(unlikelyPersonResult);
        eventsResult.getData().add(sameEvent1);
        eventsResult.getData().add(sameEvent2);
        data.setData(unlikelyPerson.getPersonID(), personsResult, eventsResult);
        ArrayList<Event> unlikelyPersonLifeList = data.getLifeEventsByPersonID(unlikelyPerson.getPersonID());
        assertNotNull(unlikelyPersonLifeList);
        assertEquals(2, unlikelyPersonLifeList.size());
        assertEquals(unlikelyPersonLifeList.get(0).getYear(), unlikelyPersonLifeList.get(1).getYear());
        assertEquals(unlikelyPersonLifeList.get(0).getEventType(), unlikelyPersonLifeList.get(1).getEventType());
    }

}
