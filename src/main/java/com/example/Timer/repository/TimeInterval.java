package com.example.Timer.repository;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "time_interval")
public class TimeInterval {
    @EmbeddedId
    private TimeIntervalKey timeIntervalKey;

    @Column(name = "end_time")
    @NotNull
    private LocalTime endTime;

    public TimeInterval(TimeIntervalKey timeIntervalKey, LocalTime endTime) {
        this.timeIntervalKey = timeIntervalKey;
        this.endTime = endTime;
    }

    public TimeIntervalKey getTimeIntervalKey() {
        return timeIntervalKey;
    }

    public void setTimeIntervalKey(TimeIntervalKey timeIntervalKey) {
        this.timeIntervalKey = timeIntervalKey;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeInterval that = (TimeInterval) o;
        return timeIntervalKey.equals(that.timeIntervalKey) && endTime.equals(that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeIntervalKey, endTime);
    }
}
