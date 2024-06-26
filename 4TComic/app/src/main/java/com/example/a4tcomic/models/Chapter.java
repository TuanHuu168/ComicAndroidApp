package com.example.a4tcomic.models;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String id;
    private String title;
    private String img_url;
    private String pdf_url;
    private int order;
    private String comic_id;
    private long created_at;

    public Chapter() {
        this("", "", "", "", 0, "", 0 );
    }

    public Chapter(String id, String title, String img_url, String pdf_url, int order, String comic_id, long created_at) {
        this.id = id;
        this.title = title;
        this.img_url = img_url;
        this.pdf_url = pdf_url;
        this.order = order;
        this.comic_id = comic_id;
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

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
