package com.example.familymapclient.server;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerProxyTest {
    private static ServerProxy server;
    private static String serverHost;
    private static String serverPort;
    private static LoginRequest validLoginRequest;
    private static LoginRequest invalidPasswordLoginRequest;
    private static LoginRequest invalidIDLoginRequest;

    private static LoginResult validLoginResult;
    private static LoginResult wrongPasswordLoginResult;
    private static LoginResult wrongIDLoginResult;

    private static String sampleAutoToken;
    private static String sampleUsername;
    private static String samplePassword;
    private static String samplePersonID;
    private static String sampleEmail;
    private static String sampleFirstName;
    private static String sampleLastName;
    private static String sampleGender;

    private static String failedLoginMessageCuzPassword;
    private static String failedLoginMessageCuzNoID;

    private static RegisterRequest validRegisterRequest;
    private static RegisterRequest invalidRegisterRequest;

    private static RegisterResult validRegisterResult;
    private static RegisterResult invalidRegisterResultCuzAlreadyUsedID;
    private static RegisterResult invalidRegisterResultCuzNotAllFieldIsFilled;

    private static String failedAlreadyRegisteredIDMessage;
    private static String failedNotRequiredFieldIsFilledMessage;

    private static String userAuthToken;
    private static String wrongAuthToken;
    private static PersonsResult vaildPeopleResult;
    private static PersonsResult wrongPeopleResult;


    @BeforeClass
    public static void settingUp() {
        server = ServerProxy.initialize();
        serverHost = "localhost";
        serverPort = "8080";
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

        sampleAutoToken = "ab64b978-b2b4-404a-8816-7c223c652008";
        sampleUsername = "sheila" + randomUsername;
        samplePassword = "parker";
        samplePersonID = "Sheila_Parker";
        sampleEmail = "k2289@byu.edu";
        sampleFirstName = "sheila";
        sampleLastName = "parker";
        sampleGender = "f";

        //REGISTER TEST SET UP
        failedAlreadyRegisteredIDMessage = "Error: We already have this username";
        failedNotRequiredFieldIsFilledMessage = "Error: Request Field Is Not Filled";
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
        failedLoginMessageCuzPassword = "Error: Password not match";
        failedLoginMessageCuzNoID = "Error: No ID that match";

        validLoginRequest = new LoginRequest(sampleUsername, "parker");
        invalidPasswordLoginRequest = new LoginRequest(sampleUsername, "1234");
        invalidIDLoginRequest = new LoginRequest("sheila11234", "1234");

        validLoginResult = new LoginResult(sampleAutoToken,
                sampleUsername, samplePersonID, true);
        wrongPasswordLoginResult = new LoginResult(failedLoginMessageCuzPassword, false);
        wrongIDLoginResult = new LoginResult(failedLoginMessageCuzNoID, false);

        //GET PEOPLE AND EVENTS TEST SET UP
        wrongAuthToken = "I will pass this exam for 100% please";
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
        assertNull(validPeopleResult.getMessage());
        assertTrue(validPeopleResult.isSuccess());
    }
}
