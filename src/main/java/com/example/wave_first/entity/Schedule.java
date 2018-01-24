package com.example.wave_first.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="PRESENTATION_ID")
    private Long presentation_id;

    @Column(name="ROOM_ID")
    private Long roomId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_TIME")
    private java.util.Date start_time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="END_TIME")
    private java.util.Date end_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPresentation_id() {
        return presentation_id;
    }

    public void setPresentation_id(Long presentation_id) {
        this.presentation_id = presentation_id;
    }

    public Long getRoom_id() {
        return roomId;
    }

    public void setRoom_id(Long room_id) {
        this.roomId = room_id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id != null ? id.equals(schedule.id) : schedule.id == null;

    }



    @Override
    public int hashCode() {

        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", presentation_id=" + presentation_id +
                ", room_id=" + roomId +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }


}
