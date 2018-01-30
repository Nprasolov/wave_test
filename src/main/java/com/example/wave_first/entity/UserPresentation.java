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
    private Long userId;

    @Column(name = "PRESENTATION_ID")
    private Long presentationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long user_id) {
        this.userId = user_id;
    }

    public Long getPresentation_id() {
        return presentationId;
    }

    public void setPresentation_id(Long presentation_id) {
        this.presentationId = presentation_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPresentation that = (UserPresentation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(presentationId, that.presentationId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, presentationId);
    }

    public UserPresentation() {

    }
    public UserPresentation(Long userId, Long presentationId) {
        this.userId = userId;
        this.presentationId = presentationId;
    }
    @Override

    public String toString() {
        return "UserPresentation{" +
                "id=" + id +
                ", user_id=" + userId +
                ", presentation_id=" + presentationId +
                '}';
    }
}
