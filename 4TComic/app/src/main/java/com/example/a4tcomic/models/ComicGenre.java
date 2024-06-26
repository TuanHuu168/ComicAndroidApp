package com.example.a4tcomic.models;

public class ComicGenre {
    private String comic_id;
    private String genre_id;
    private String id;

    public ComicGenre(String comic_id, String genre_id, String id) {
        this.comic_id = comic_id;
        this.genre_id = genre_id;
        this.id = id;
    }

    public ComicGenre(){
        this("","","");
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
