package com.example.wave_first.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "PRESENTATION_ID")
    private Long presentation_id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPresentation that = (UserPresentation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(presentation_id, that.presentation_id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user_id, presentation_id);
    }

    @Override
    public String toString() {
        return "UserPresentation{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", presentation_id=" + presentation_id +
                '}';
    }
}
