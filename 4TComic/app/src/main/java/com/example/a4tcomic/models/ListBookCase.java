package com.example.a4tcomic.models;

public class ListBookCase {

    private  String imageBook;
    private String nameBook;
    private boolean ischecked;
    private int orderChapter;

    public ListBookCase(String imageBook, String nameBook, int orderChapter) {
        this.imageBook = imageBook;
        this.nameBook = nameBook;
        this.ischecked = false;
        this.orderChapter = orderChapter;
    }

    public ListBookCase(String imageBook, String nameBook) {
        this.imageBook = imageBook;
        this.nameBook = nameBook;
        this.ischecked = false;
    }

    public String getImageBook() {
        return imageBook;
    }

    public void setImageBook(String imageBook) {
        this.imageBook = imageBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }
    public boolean isChecked() {
        return ischecked;
    }

    public void setChecked(boolean checked) {
        ischecked = checked;
    }

    public int getOrderChapter() {
        return orderChapter;
    }

    public void setOrderChapter(int orderChapter) {
        this.orderChapter = orderChapter;
    }
}
