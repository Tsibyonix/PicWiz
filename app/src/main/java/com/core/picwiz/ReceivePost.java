package com.core.picwiz;

import java.util.List;
import java.util.Map;

/**
 * Created by chronix on 14/4/16.
 */
public class ReceivePost {

    private int success;

    /**
     * likes : 0
     * time : 14-4-2016-4-41-52
     * privacy : private
     * image : uploads/img-14-4-2016-4-41-52.jpg
     * comments : {"d4N73":"thanks","chronix":"Great picture"}
     * liked_by : []
     * id : 02d41d57-27fd-4de4-b0b4-8e91a2b48710
     * username : d4N73
     * caption : i am working.
     * location : 28, Rohtak RdIndira Colony, Tri NagarNew Delhi, Delhi 110035
     * tags : ["working"]
     */

    private List<OutputEntity> output;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<OutputEntity> getOutput() {
        return output;
    }

    public void setOutput(List<OutputEntity> output) {
        this.output = output;
    }

    public static class OutputEntity {
        private int likes;
        private String time;
        private String privacy;
        private String image;
        private Map<String, String> comments;
        private String id;
        private String username;
        private String tag_line;
        private String caption;
        private String location;
        private String location_type;
        private List<String> liked_by;
        private List<String> tags;

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTag_line() {
            return tag_line;
        }

        public void setTag_line(String tag_line) {
            this.tag_line = tag_line;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setComments(Map<String, String> comments){
            this.comments = comments;
        }

        public Map<String, String> getComments() {
            return comments;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation_type() {
            return location_type;
        }

        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }

        public List<String> getLiked_by() {
            return liked_by;
        }

        public void setLiked_by(List<String> liked_by) {
            this.liked_by = liked_by;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
