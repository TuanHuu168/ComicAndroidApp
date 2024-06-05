package com.example.a4tcomic.models;

public class ContentItem {
    public static final int TYPE_COMIC = 0;
    public static final int TYPE_ACCOUNT = 1;
    public static final int TYPE_UPDATE = 2;

    private int type;
    private int imageResource;
    private String title;

    public ContentItem(int type, int imageResource, String title) {
        this.type = type;
        this.imageResource = imageResource;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }
}
