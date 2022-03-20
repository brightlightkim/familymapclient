package com.example.familymapclient.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Model.AuthToken;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class ServerProxy {

    private static ServerProxy serverProxy;
    private String serverHost;
    private String serverPort;
    private Gson gson = new Gson();

    public synchronized static ServerProxy initialize()
    {
        if (serverProxy == null){
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest request) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String requestInfo = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(requestInfo, reqBody);
            reqBody.close();
            LoginResult result = null;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, LoginResult.class);
            }
            else {
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, LoginResult.class);
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult register(RegisterRequest request){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String requestInfo = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(requestInfo, reqBody);
            reqBody.close();
            RegisterResult result = null;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, RegisterResult.class);
            }
            else {
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, RegisterResult.class);
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PersonsResult getPeople(String authToken){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            PersonsResult result = null;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, PersonsResult.class);
            }
            else {
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, PersonsResult.class);
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EventsResult getEvents(String authToken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            EventsResult result = null;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, EventsResult.class);
            }
            else {
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, EventsResult.class);
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
