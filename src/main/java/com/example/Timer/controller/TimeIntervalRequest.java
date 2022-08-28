package com.example.Timer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TimeIntervalRequest {
    @JsonProperty
    private LocalDateTime startTime;
    @JsonProperty
    private LocalDateTime endTime;

    public TimeIntervalRequest(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeIntervalRequest() {
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
