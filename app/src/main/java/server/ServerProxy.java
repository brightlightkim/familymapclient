package server;

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
    private Gson gson;

    public static ServerProxy initialize()
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
            http.connect();//TODO: Question why this causes an error message.
            //So I set a breakpoint here and in some reasons, it gets error,
            //and throw my application.. Can you help me out with this please?
            //Sorry just wait for a second please. my computer is slow.
            //Can you see this??

            String requestInfo = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(requestInfo, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println(respData);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult register(RegisterRequest request){
        return null;
    }

    public PersonsResult getPeople(AuthToken authToken){
        return null;
    }

    public EventsResult getEvents(AuthToken authToken) {
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
