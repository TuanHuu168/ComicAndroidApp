package com.example.a4tcomic;

public class CommentItem {
    private int imageResource;
    private String title;
    private String user;
    private String content;
    private String time;

    public CommentItem(int imageResource, String title, String user, String content, String time) {
        this.imageResource = imageResource;
        this.title = title;
        this.user = user;
        this.content = content;
        this.time = time;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
