package com.example.fna;

import java.io.Serializable;

public class Notes implements Serializable {
    private long id;
    private String title;
    private String text;
    private String tag;

    public Notes (long id, String title, String text, String tag) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTexte() {
        return text;
    }

    public String getTag(){
        return tag;
    }
}
