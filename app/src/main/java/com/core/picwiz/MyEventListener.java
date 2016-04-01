package com.core.picwiz;

import org.json.JSONObject;

/**
 * Created by chronix on 1/4/16.
 */
public interface MyEventListener {
    public void onEventCompleted(JSONObject jsonObject);
    public void onEventFailed(JSONObject jsonObject);
}
