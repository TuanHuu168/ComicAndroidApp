package com.example.a4tcomic.models;

//import com.google.type.DateTime;

public class Comic {
    private String id;
    private String title;
    private String img_url;
    private String banner_url;
    private String author_id;
    private String description;
    private String user_id;
    private long created_at;

    public Comic() {
        this("", "", "", "", "", "", "", 0);
    }

    public Comic(String id, String title, String img_url, String banner_url, String author_id, String description, String user_id, long created_at) {
        this.id = id;
        this.title = title;
        this.img_url = img_url;
        this.banner_url = banner_url;
        this.author_id = author_id;
        this.description = description;
        this.user_id = user_id;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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