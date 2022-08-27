package com.example.Timer.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeMap;

public class TimerService {
    public TreeMap<LocalDate, Long> timeElapsed(LocalDateTime startTime, LocalDateTime endTime) {
        TreeMap<LocalDate, Long> timeElapsed = new TreeMap<>();
        long duration;

        while (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            LocalDateTime startNextDay = startTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            duration = Duration.between(startTime, startNextDay).getSeconds();
            timeElapsed.put(startTime.toLocalDate(), duration);
            startTime = startNextDay;
        }

        duration = Duration.between(startTime, endTime).getSeconds();
        timeElapsed.put(startTime.toLocalDate(), duration);

        return timeElapsed;
    }
}
