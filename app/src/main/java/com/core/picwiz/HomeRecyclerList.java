package com.core.picwiz;

/**
 * Created by chronix on 4/4/16.
 */
public class HomeRecyclerList {
    public String username;
    public String tagLine;
    public String time;
    public String tags;
    public int photoId;
    public String caption;
    public String location;
    public String comments;

    HomeRecyclerList(String username, String tagLine, String time, String tags, int photoId, String caption, String location, String comments) {
        this.username = username;
        this.tagLine = tagLine;
        this.time = time;
        this.tags = tags;
        this.photoId = photoId;
        this.caption = caption;
        this.location = location;
        this.comments = comments;
    }
}
