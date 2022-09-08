package com.example.Timer.controller;

import com.example.Timer.repository.TimeInterval;

import java.time.LocalTime;
import java.util.Objects;

public class TimeIntervalDTO {
    private LocalTime start;
    private LocalTime end;

    public TimeIntervalDTO(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public static TimeIntervalDTO from(TimeInterval timeInterval) {
        return new TimeIntervalDTO(timeInterval.getTimeIntervalKey().getStartTime(),
                timeInterval.getEndTime());
    }

    public TimeIntervalDTO() {
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeIntervalDTO that = (TimeIntervalDTO) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
