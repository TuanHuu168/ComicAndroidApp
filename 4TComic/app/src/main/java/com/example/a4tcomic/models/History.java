package com.example.a4tcomic.models;

public class History {
    private String id;
    private String user_id;
    private String chapter_id;
    private int last_date;

    public History(String id, String user_id, String chapter_id, int last_date) {
        this.id = id;
        this.user_id = user_id;
        this.chapter_id = chapter_id;
        this.last_date = last_date;
    }

    public History() {
        this("", "", "", 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getLast_date() {
        return last_date;
    }

    public void setLast_date(int last_date) {
        this.last_date = last_date;
    }
}
