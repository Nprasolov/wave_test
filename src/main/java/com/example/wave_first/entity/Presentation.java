package com.example.wave_first.entity;

import javax.persistence.*;

@Entity
public class Presentation {
    @Basic

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String title;

    @Column
    private String theme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Presentation(int id, String title, String theme) {
        this.id = id;
        this.title = title;
        this.theme = theme;
    }
}
