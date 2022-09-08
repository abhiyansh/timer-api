package com.example.Timer.controller;

import com.example.Timer.repository.TimeIntervalKey;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeInterval {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime end;

    public DateTimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public DateTimeInterval(TimeIntervalKey timeIntervalKey, LocalTime of) {
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}