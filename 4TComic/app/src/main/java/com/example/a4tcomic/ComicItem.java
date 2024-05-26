package com.example.a4tcomic;

public class ComicItem {
    private int imageResource;
    private String title;

    public ComicItem(int imageResource, String title) {
        this.imageResource = imageResource;
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }
}
