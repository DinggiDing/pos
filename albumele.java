package com.example.albumtab_spinner;

public class albumele {
    private int id;
    private int memo;
    private String image;

    public albumele(int memo, String image, int id) {
        this.memo = memo;
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemo() {
        return memo;
    }

    public void setMemo(int name) {
        this.memo = memo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
