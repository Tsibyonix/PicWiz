package com.core.picwiz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PicWizBackend implements MyEventListener {
    //private static final String ADDRESS = "http://192.168.1.4:8000/";
    //private static final List<String> SERVICE = Arrays.asList("register", "login", "task");
    //private static final String PORT = "8000";

    private Context context;
    private OkHttpClient client;

    private Boolean responseReceived = false;

    private int success = 0;
    private String message = "Unknown Error";
    private String host = "none";
    private String id = null;
    private String username = null;
    private String service = null;

    public PicWizBackend(Context context) {
        this.context = context;
        client = new OkHttpClient();
    }

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public String getHost() {
        return host;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getWait() {
        return responseReceived;
    }

    public String getService() {
        return service;
    }

    public void setResponseReceivedFalse() {
        responseReceived = false;
    }

    public void register(String email, String password, String username) {
        service = "register";
        Log.i("register: ", "function init");
        Task registerTask = new Task(this, email, password, username);
        registerTask.execute((Void) null);
    }

    public void login(String email, String password) {
        service = "login";
        Log.i("login: ", "function init");
        Task loginTest = new Task(this, email, password);
        loginTest.execute((Void) null);
    }

    public void resetBackend() {

    }

    @Override
    public void onEventCompleted(JSONObject jsonObject) {
        try {
            message = jsonObject.getString("message");
            success = jsonObject.getInt("success");
            host = jsonObject.getString("host");
            if (success == 1) {
                id = jsonObject.getString("id");
                if (Objects.equals(service, "register"))
                    username = jsonObject.getString("username");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("register: ", e.getMessage());
        }
        Log.i("register", "setting response received");
        responseReceived = true;
    }

    @Override
    public void onEventFailed(JSONObject jsonObject) {

        if (jsonObject == null) {
            message = "Unknown Error occurred, no response from server.";
            success = 0;
        } else {
            try {
                host = jsonObject.getString("host");
                message = jsonObject.getString("message");
                success = jsonObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("register: ", e.getMessage());
            }
        }
        responseReceived = false;
    }

    class Task extends AsyncTask<Void, Void, String> {
        private MyEventListener callback;
        String email = null;
        String password = null;
        String username = null;

        public Task(MyEventListener cb, String email, String password, String username) {
            callback = cb;
            this.email = email;
            this.password = password;
            this.username = username;
        }

        public Task(MyEventListener cb, String email, String password) {
            callback = cb;
            this.email = email;
            this.password = password;
        }

        private String run() throws IOException {
            String url;
            if (Objects.equals(service, "register")) {
                url = "http://192.168.1.4:8000/register?email=" + email + "&password=" + password + "&username=" + username;
            } else {
                url = "http://192.168.1.4:8000/login?email=" + email + "&password=" + password;
            }

            Log.i("register: ", "in run: "+url);
            Request register = new Request.Builder()
                    .url(url)
                    // http://192.168.1.4:8000/register?email=san@san&password=123456&username=chronix
                    .build();
            Response registerResponse = client.newCall(register).execute();
            if (!registerResponse.isSuccessful()) throw new IOException("Unexpected code " + registerResponse);
            return registerResponse.body().string();
        }

        @Override
        protected String doInBackground(Void... params) {
            String temp = null;
            try {
                temp = run();
                Log.i("register: ", "response received");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("register: ", e.getMessage());
            }
            return temp;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                callback.onEventCompleted(jsonObject);
                Log.i("register: ", jsonObject.getString("message"));
            } catch (JSONException e) {
                callback.onEventFailed(null);
                e.printStackTrace();
                Log.i("register: ", e.getMessage());
            }
        }
    }
}
