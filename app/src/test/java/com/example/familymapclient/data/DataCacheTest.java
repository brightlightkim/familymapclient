package com.example.familymapclient.data;

import com.example.familymapclient.helperModel.PersonWithRelationship;
import com.example.familymapclient.server.ServerProxy;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;

/**
 * This test is after the load of the given data of Sheila Parker
 * This test is for the public functions of DataCache and test the helper functions
 * by its variable counts
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataCacheTest {
    private static DataCache data;
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
    public void Test1_setDateFailed(){
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
    public void Test2_setDataSuccess() {
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
    public void Test3_findPeopleWithTextSuccess() {
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
    public void Test4_findPeopleWithTextFailed() {
        assertNotNull(data.findPeopleWithText("greg"));
        assertNotNull(data.findPeopleWithText("sha"));
        assertEquals(0, data.findPeopleWithText("greg").size());
        assertEquals(0, data.findPeopleWithText("sha").size());
    }

    @Test
    public void Test5_findEventsWithTextSucceed() {
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
    public void Test6_findEventsWithTextFailed(){
        assertNotNull(data.findEventsWithText("greg"));
        assertNotNull(data.findEventsWithText("sha"));
        assertEquals(0, data.findEventsWithText("greg").size());
        assertEquals(0, data.findEventsWithText("sha").size());
    }

    @Test
    public void Test7_getFamilyWithRelationship() {
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
    public void Test8_getFamilyWithRelationshipFailed() {
        ArrayList<PersonWithRelationship> nonExistFamily = data.getFamilyWithRelationship(notExistPerson);
        assertNotNull(nonExistFamily );
        assertEquals(0,nonExistFamily.size());
    }

}