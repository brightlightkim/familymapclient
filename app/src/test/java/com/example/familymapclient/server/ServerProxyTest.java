package com.example.familymapclient.server;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerProxyTest {
    private static ServerProxy server;
    private static LoginRequest validLoginRequest;
    private static LoginRequest invalidPasswordLoginRequest;
    private static LoginRequest invalidIDLoginRequest;

    private static LoginResult validLoginResult;
    private static LoginResult wrongPasswordLoginResult;
    private static LoginResult wrongIDLoginResult;

    private static RegisterRequest validRegisterRequest;
    private static RegisterRequest invalidRegisterRequest;

    private static RegisterResult validRegisterResult;
    private static RegisterResult invalidRegisterResultCuzAlreadyUsedID;
    private static RegisterResult invalidRegisterResultCuzNotAllFieldIsFilled;

    private static String userAuthToken;
    private static String wrongAuthToken;
    private static String notMatchAuthTokenResultMessage;


    @BeforeClass
    public static void setUp() {
        server = ServerProxy.initialize();
        String serverHost = "localhost";
        String serverPort = "8080";
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
        //SAMPLE DATA SET UP

        //Setting a random ID
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }
        String randomUsername = sb.toString();

        String sampleAutoToken = "ab64b978-b2b4-404a-8816-7c223c652008";
        String sampleUsername = "sheila" + randomUsername;
        String samplePassword = "parker";
        String samplePersonID = "Sheila_Parker";
        String sampleEmail = "k2289@byu.edu";
        String sampleFirstName = "sheila";
        String sampleLastName = "parker";
        String sampleGender = "f";

        //REGISTER TEST SET UP
        String failedAlreadyRegisteredIDMessage = "Error: We already have this username";
        String failedNotRequiredFieldIsFilledMessage = "Error: Request Field Is Not Filled";
        validRegisterRequest = new RegisterRequest(
                sampleUsername, samplePassword, sampleEmail,
                sampleFirstName, sampleLastName, sampleGender);
        invalidRegisterRequest = new RegisterRequest(
                null, null, null, null, null, null);
        validRegisterResult = new RegisterResult(
                sampleAutoToken, sampleUsername, samplePersonID, true);
        invalidRegisterResultCuzAlreadyUsedID = new RegisterResult(
                failedAlreadyRegisteredIDMessage, false);
        invalidRegisterResultCuzNotAllFieldIsFilled = new RegisterResult(
                failedNotRequiredFieldIsFilledMessage, false
        );

        //LOGIN TEST SET UP
        String failedLoginMessageCuzPassword = "Error: Password not match";
        String failedLoginMessageCuzNoID = "Error: No ID that match";

        validLoginRequest = new LoginRequest(sampleUsername, "parker");
        invalidPasswordLoginRequest = new LoginRequest(sampleUsername, "1234");
        invalidIDLoginRequest = new LoginRequest("sheila11234", "1234");

        validLoginResult = new LoginResult(sampleAutoToken,
                sampleUsername, samplePersonID, true);
        wrongPasswordLoginResult = new LoginResult(failedLoginMessageCuzPassword, false);
        wrongIDLoginResult = new LoginResult(failedLoginMessageCuzNoID, false);

        //GET PEOPLE AND EVENTS TEST SET UP
        wrongAuthToken = "I will pass this exam for 100% please";
        notMatchAuthTokenResultMessage = "Error: No Token that match";
    }

    @Test
    public void Test1_validRegister() {
        RegisterResult validRegisterOutput = server.register(validRegisterRequest);
        userAuthToken = validRegisterOutput.getAuthtoken();
        assertNotNull(validRegisterOutput);
        assertNull(validRegisterOutput.getMessage());
        assertEquals(validRegisterResult.getUsername(), validRegisterOutput.getUsername());
        assertEquals(validRegisterResult.isSuccess(), validRegisterOutput.isSuccess());
        assertNotNull(validRegisterOutput.getAuthtoken());
        assertNotNull(validRegisterOutput.getPersonID());
    }

    @Test
    public void Test2_invalidRegisterWithAlreadyUsedID() {
        RegisterResult invalidRegisterOutput = server.register(validRegisterRequest);
        assertNotNull(invalidRegisterOutput);
        assertNull(invalidRegisterOutput.getPersonID());
        assertNull(invalidRegisterOutput.getAuthtoken());
        assertNull(invalidRegisterOutput.getUsername());
        assertEquals(invalidRegisterResultCuzAlreadyUsedID.getMessage(), invalidRegisterOutput.getMessage());
    }

    @Test
    public void Test3_invalidRegisterWithNoValidInput() {
        RegisterResult invalidRegisterOutput = server.register(invalidRegisterRequest);
        assertNull(invalidRegisterOutput.getAuthtoken());
        assertNull(invalidRegisterOutput.getUsername());
        assertNull(invalidRegisterOutput.getPersonID());
        assertEquals(invalidRegisterResultCuzNotAllFieldIsFilled.getMessage(),
                invalidRegisterOutput.getMessage());
        assertEquals(invalidRegisterResultCuzNotAllFieldIsFilled.isSuccess(),
                invalidRegisterOutput.isSuccess());
    }

    @Test
    public void Test4_validLogin() {
        LoginResult validSampleLoginResult = server.login(validLoginRequest);
        assertNull(validSampleLoginResult.getMessage());
        assertNotNull(validSampleLoginResult.getAuthtoken());
        assertEquals(validLoginResult.getUsername(), validSampleLoginResult.getUsername());
        assertEquals(validLoginResult.isSuccess(), validSampleLoginResult.isSuccess());
    }

    @Test
    public void Test5_wrongPasswordLogin() {
        LoginResult validSampleLoginResult = server.login(invalidPasswordLoginRequest);
        assertNull(validSampleLoginResult.getAuthtoken());
        assertNull(validSampleLoginResult.getUsername());
        assertNull(validSampleLoginResult.getPersonID());
        assertEquals(wrongPasswordLoginResult.getMessage(), validSampleLoginResult.getMessage());
        assertEquals(wrongPasswordLoginResult.isSuccess(), validSampleLoginResult.isSuccess());
    }

    @Test
    public void Test6_wrongIDLogin() {
        LoginResult validSampleLoginResult = server.login(invalidIDLoginRequest);
        assertNull(validSampleLoginResult.getAuthtoken());
        assertNull(validSampleLoginResult.getUsername());
        assertNull(validSampleLoginResult.getPersonID());
        assertEquals(wrongIDLoginResult.getMessage(), validSampleLoginResult.getMessage());
        assertEquals(wrongIDLoginResult.isSuccess(), validSampleLoginResult.isSuccess());
    }

    @Test
    public void Test7_validGetPeople() {
        PersonsResult validPeopleResult = server.getPeople(userAuthToken);
        assertNotNull(validPeopleResult);
        assertNotNull(validPeopleResult.getData());
        assertNull(validPeopleResult.getMessage());
        assertTrue(validPeopleResult.isSuccess());
    }

    @Test
    public void Test8_invalidGetPeople() {
        PersonsResult invalidPeopleResult = server.getPeople(wrongAuthToken);
        assertNotNull(invalidPeopleResult);
        assertNull(invalidPeopleResult.getData());
        assertNotNull(invalidPeopleResult.getMessage());
        assertEquals(notMatchAuthTokenResultMessage, invalidPeopleResult.getMessage());
        assertFalse(invalidPeopleResult.isSuccess());
    }

    @Test
    public void Test9_validGetEvents(){
        EventsResult validEventsResult = server.getEvents(userAuthToken);
        assertNotNull(validEventsResult);
        assertNotNull(validEventsResult.getData());
        assertNull(validEventsResult.getMessage());
        assertTrue(validEventsResult.isSuccess());
    }

    @Test
    public void Test10_invalidGetEvents(){
        EventsResult invalidEventsResult = server.getEvents(wrongAuthToken);
        assertNotNull(invalidEventsResult);
        assertNull(invalidEventsResult.getData());
        assertNotNull(invalidEventsResult.getMessage());
        assertEquals(notMatchAuthTokenResultMessage, invalidEventsResult.getMessage());
        assertFalse(invalidEventsResult.isSuccess());
    }
}
