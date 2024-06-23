package com.example.a4tcomic.models;

public class Favorite {
    private String id;
    private String comic_id;
    private String user_id;
    private int created_at;

    public Favorite(String id, String comic_id, String user_id, int created_at) {
        this.id = id;
        this.comic_id = comic_id;
        this.user_id = user_id;
        this.created_at = created_at;
    }

    public Favorite() {
        this("","","",0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }
}
