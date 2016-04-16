package com.core.picwiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private String name = null;
    private String tagline = null;
    private String service = null;
    private int followers = 0;
    private int following = 0;
    private String imageName = null;
    private String imageString = null;
    private Gson gson;
    private ReceivePost receivePost;

    private final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

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

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public ReceivePost getReceivePost() {
        return receivePost;
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

    private static final String[] servicesProvided = {
            "register",
            "login",
            "update",
            "authenticate",
            "post",
            "get"
    };

    public void register(String email, String password, String username) {
        service = servicesProvided[0];
        Log.i("register: ", "function init");
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("username", username);
        RequestBody body = RequestBody.create(JSON, new JSONObject(map).toString());
        Task registerTask = new Task(this, body, email, password, username);
        registerTask.execute((Void) null);
    }

    public void login(String email, String password) {
        service = servicesProvided[1];
        Log.i("login: ", "function init");
        Task loginTask = new Task(this, email, password);
        loginTask.execute((Void) null);
    }

    public void update(String email, String name, String tagline) {
        service =  servicesProvided[2];
        Log.i("update: ", "function init");
        Task updateTask = new Task(this, email, name, tagline, null);
        updateTask.execute((Void) null);
    }

    public void createPost(String username, String tag_line, String tags, String caption, Bitmap image, String location, String locationType, String privacy, String time) {
        service = servicesProvided[4];
        Log.i("post: ", "function init");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.URL_SAFE);
        HashMap<String, String> map = new HashMap<>();
        map.put("image", "img-"+time+".jpg");
        map.put("username", username);
        map.put("tag_line", tag_line);
        map.put("tags", tags);
        map.put("caption", caption);
        map.put("location", location);
        map.put("locationType", locationType);
        map.put("privacy", privacy);
        map.put("time", time);

        Log.i("json: ", new JSONObject(map).toString());
        RequestBody body = RequestBody.create(JSON, new JSONObject(map).toString());

        Task postTask = new Task(this, body);
        postTask.execute((Void) null);
    }

    void postImage() {
        HashMap<String, String> map = new HashMap<>();
        if (imageName != null && imageString != null) {
            map.put("image_name", imageName);
            map.put("image", imageString);
        }
            final RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                "http://192.168.1.4:8000/image", new JSONObject(map), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    message = response.getString("message");
                    success = response.getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
            requestQueue.add(request);

    }

    void getPost(String username) {
        service = servicesProvided[5];
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        RequestBody body = RequestBody.create(JSON, new JSONObject(map).toString());
        Task getPost = new Task(this, body);
        getPost.execute();
    }

    @Override
    public void onEventCompleted(JSONObject jsonObject) {
        try {
            switch (service) {
                case "register":
                    message = jsonObject.getString("message");
                    success = jsonObject.getInt("success");
                    host = jsonObject.getString("host");
                    if (success == 1)
                        id = jsonObject.getString("id");
                    break;
                case "login":
                    message = jsonObject.getString("message");
                    success = jsonObject.getInt("success");
                    host = jsonObject.getString("host");
                    if (success == 1) {
                        id = jsonObject.getString("id");
                        username = jsonObject.getString("username");
                        name = jsonObject.getString("name");
                        tagline = jsonObject.getString("tagline");
                        followers = jsonObject.getInt("follower");
                        following = jsonObject.getInt("following");
                        Log.i("followers: following, ", String.valueOf(followers)+": "+String.valueOf(following));
                    }
                    break;
                case "update":
                    message = jsonObject.getString("message");
                    success = jsonObject.getInt("success");
                    host = jsonObject.getString("host");
                    break;
                case "post":
                    message = jsonObject.getString("message");
                    success = jsonObject.getInt("success");
                    host = jsonObject.getString("host");
                    if (success == 1) {
                        imageName = jsonObject.getString("image");
                    }
                    break;
                case "get":
                    //receivePost = new ReceivePost();
                    gson = new Gson();
                    receivePost = gson.fromJson(jsonObject.toString(), ReceivePost.class);
                    success = receivePost.getSuccess();
                    break;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i(service+": ", e.getMessage());
        }
        Log.i("register", "setting response received");
        responseReceived = true;
    }

    @Override
    public void onEventFailed(JSONObject jsonObject) {

        if (jsonObject == null) {
            message = "Unknown Error occurred, no response from server.";
            host = "None";
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
        String name = null;
        String tagline = null;

        String userID = null;
        String tags = null;
        String caption = null;
        String image = null;
        String location = null;
        String privacy = null;
        String timeStamp = null;
        RequestBody requestBody;

        public Task(MyEventListener cb, RequestBody body, String arg1, String arg2, String arg3) {
            //register
            callback = cb;
            this.email = arg1;
            this.password = arg2;
            this.username = arg3;
        }

        public Task(MyEventListener cb, String arg1, String arg2) {
            //login
            callback = cb;
            this.email = arg1;
            this.password = arg2;
        }

        public Task(MyEventListener cb, String arg1, String arg2, String arg3, String arg4) {
            callback = cb;
            this.email = arg1;
            this.name = arg2;
            this.tagline = arg3;
        }

        public Task(MyEventListener cb, RequestBody arg1) {
            //post
            callback = cb;
            requestBody = arg1;
        }

        private String run() throws IOException {
            String url = null;
            switch (service) {
                case "register":
                    HttpUrl.Builder register = HttpUrl.parse("http://192.168.1.4:8000/register").newBuilder();
                    register.addQueryParameter("email", email);
                    register.addQueryParameter("password", password);
                    register.addQueryParameter("username", username);
                    url = register.build().toString();
                    Log.i("url: ", url);
                    break;
                case "login":
                    HttpUrl.Builder login = HttpUrl.parse("http://192.168.1.4:8000/login").newBuilder();
                    login.addQueryParameter("email", email);
                    login.addQueryParameter("password", password);
                    url = login.build().toString();
                    Log.i("url: ", url);
                    break;
                case "update":
                    HttpUrl.Builder update = HttpUrl.parse("http://192.168.1.4:8000/update").newBuilder();
                    update.addQueryParameter("email", email);
                    update.addQueryParameter("name", name);
                    update.addQueryParameter("tagline", tagline);
                    url =  update.build().toString();
                    Log.i("url: ", url);
                    break;
                case "post":
                    HttpUrl.Builder post = HttpUrl.parse("http://192.168.1.4:8000/post").newBuilder();
                    url = post.build().toString();
                    break;
                case "get":
                    HttpUrl.Builder get = HttpUrl.parse("http://192.168.1.4:8000/get_post").newBuilder();
                    url = get.build().toString();
            }
            assert url != null;
            Request register;
            if (service == "post" || service == "get") {
                register = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Log.i("url: ", url);
            } else {
                register = new Request.Builder()
                        .url(url)
                        .build();
            }
            Response registerResponse = client.newCall(register).execute();
            if (!registerResponse.isSuccessful()) throw new IOException("Unexpected code " + registerResponse);
            //registerResponse.body().close();
            return registerResponse.body().string();
        }

        @Override
        protected String doInBackground(Void... params) {
            String temp = null;
            try {
                temp = run();
                Log.i(service+": ", "response received");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(service+": ", e.getMessage());
            }
            return temp;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            if (s == null) {
                callback.onEventFailed(null);
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                callback.onEventCompleted(jsonObject);
                Log.i(service+": ", jsonObject.getString("message"));
            } catch (JSONException e) {
                callback.onEventFailed(null);
                e.printStackTrace();
                Log.i(service+": ", e.getMessage());
            }
        }
    }
}
