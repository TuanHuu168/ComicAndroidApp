package com.example.a4tcomic.models;

public class CommentItem {
    private  String comic;
    private String user;
    private String content;
    private Long time;

    public CommentItem(String comic, String user, String content, Long time) {
        this.user = user;
        this.content = content;
        this.time = time;
    }

    public String getComic() {
        return comic;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Long getTime() {
        return time;
    }
}
