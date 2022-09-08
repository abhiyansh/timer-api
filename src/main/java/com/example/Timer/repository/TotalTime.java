package com.example.Timer.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "total_time")
public class TotalTime {
    @Id
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @Column
    @NotNull
    private long time;

    public TotalTime(LocalDate date, long time) {
        this.date = date;
        this.time = time;
    }

    public TotalTime() {
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalTime totalTime = (TotalTime) o;
        return time == totalTime.time && date.equals(totalTime.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }
}
