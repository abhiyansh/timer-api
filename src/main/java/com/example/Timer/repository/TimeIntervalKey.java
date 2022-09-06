package com.example.Timer.repository;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class TimeIntervalKey implements Serializable {
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    public TimeIntervalKey(LocalDate date, LocalTime startTime) {
        this.date = date;
        this.startTime = startTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeIntervalKey that = (TimeIntervalKey) o;
        return date.equals(that.date) && startTime.equals(that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime);
    }
}
