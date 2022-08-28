package com.example.Timer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeOffSetRequest {
    @JsonProperty
    private long timeOffSet;

    public TimeOffSetRequest(long timeOffSet) {
        this.timeOffSet = timeOffSet;
    }

    public TimeOffSetRequest() {
    }

    public long getTimeOffSet() {
        return timeOffSet;
    }
}
