package com.example.comicandroidapp.story_detail;

public class Chapter {
    private int resourceId;
    private String name;
    private String time_update;

    public Chapter(int resourceId, String name, String time_update) {
        this.resourceId = resourceId;
        this.name = name;
        this.time_update = time_update;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeUpdate() {
        return time_update;
    }

    public void setTimeUpdate(String time_update) {
        this.time_update = time_update;
    }
}
