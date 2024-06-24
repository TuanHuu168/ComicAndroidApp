package com.example.a4tcomic.models;

public class Comment {
    private String id;
    private String body;
    private String comic_id;
    private String user_id;
    private long created_at;

    public Comment(String id, String body, String comic_id, String user_id, long created_at) {
        this.id = id;
        this.body = body;
        this.comic_id = comic_id;
        this.user_id = user_id;
        this.created_at = created_at;
    }

    // Default constructor for Firebase
    public Comment() {
        this("", "", "", "", 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
