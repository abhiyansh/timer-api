package com.example.Timer.repository;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "time_interval")
public class TimeInterval {
    @EmbeddedId
    private TimeIntervalKey timeIntervalKey;

    @Column(name = "end_time")
    @NotNull
    private LocalTime endTime;

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
}
