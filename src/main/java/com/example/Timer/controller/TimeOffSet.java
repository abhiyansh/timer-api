package com.example.Timer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeOffSet {
    @JsonProperty
    private long timeOffSet;

    public TimeOffSet(long timeOffSet) {
        this.timeOffSet = timeOffSet;
    }

    public TimeOffSet() {
    }

    public long getTimeOffSet() {
        return timeOffSet;
    }
}
