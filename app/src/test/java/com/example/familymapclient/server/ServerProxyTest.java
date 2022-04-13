package com.example.familymapclient.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import Request.LoginRequest;
import Result.LoginResult;

public class ServerProxyTest {
    private ServerProxy server;
    private String serverHost;
    private String serverPort;
    private LoginRequest validLoginRequest;
    private LoginRequest invalidLoginRequest;
    private LoginResult validLoginResult;
    private LoginResult failedLoginResult;
    private String validLoginAutoToken;
    private String validLoginUsername;
    private String validLoginPersonID;

    @Before
    public void settingUp() {
        server = ServerProxy.initialize();
        serverHost = "localhost";
        serverPort = "8080";
        server.setServerHost(serverHost);
        server.setServerPort(serverPort);
        validLoginRequest = new LoginRequest("sheila", "parker");
        invalidLoginRequest = new LoginRequest("sheila", "1234");
        validLoginAutoToken = "ab64b978-b2b4-404a-8816-7c223c652008";
        validLoginUsername = "sheila";
        validLoginPersonID = "Sheila_Parker";
        validLoginResult = new LoginResult(validLoginAutoToken,
                validLoginUsername, validLoginPersonID, true);
        failedLoginResult = new LoginResult();
    }

    @Test
    public void test1() {
        assertEquals("hello", "hello");
    }
}
