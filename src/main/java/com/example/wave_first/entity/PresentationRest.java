package com.example.wave_first.entity;

public class PresentationRest {
    private Long id;
    private Long user_id;
    private Long presentation_id;
    private String username;
    private String presentation_title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPresentation_id() {
        return presentation_id;
    }

    public void setPresentation_id(Long presentation_id) {
        this.presentation_id = presentation_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPresentation_title() {
        return presentation_title;
    }

    public void setPresentation_title(String presentation_title) {
        this.presentation_title = presentation_title;
    }
}
