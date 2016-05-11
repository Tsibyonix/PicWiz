package com.core.picwiz;

import java.util.Map;

/**
 * Created by chronix on 19/4/16.
 */
public class RefreshComment {

    /**
     * output : {"chronix":"this is the first comment."}
     * success : 1
     */

    private int success;
    private Map<String, String> comments;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setComments(Map<String, String> comments){
        this.comments = comments;
    }

    public Map<String, String> getComments() {
        return comments;
    }
}
