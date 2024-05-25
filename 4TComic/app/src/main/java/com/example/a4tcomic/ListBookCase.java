package com.example.a4tcomic;

public class ListBookCase {

    private  int imageBook;
    private String nameBook;
    private boolean ischecked;

    public ListBookCase(int imageBook, String nameBook) {
        this.imageBook = imageBook;
        this.nameBook = nameBook;
        this.ischecked = false;
    }

    public int getImageBook() {
        return imageBook;
    }

    public void setImageBook(int imageBook) {
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
}
