package com.example.Timer.service;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.repository.TotalTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TimerService {
    private final TotalTimeRepository totalTimeRepository;

    @Autowired
    public TimerService(TotalTimeRepository totalTimeRepository) {
        this.totalTimeRepository = totalTimeRepository;
    }

    void addInterval(LocalDateTime startTime, LocalDateTime endTime) {
        ArrayList<TotalTime> timeElapsed = new ArrayList<>();
        long duration;

        while (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            LocalDateTime startNextDay = startTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            duration = Duration.between(startTime, startNextDay).getSeconds();
            duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                    .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                    .getTime();
            timeElapsed.add(new TotalTime(startTime.toLocalDate(), duration));
            startTime = startNextDay;
        }

        duration = Duration.between(startTime, endTime).getSeconds();
        duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                .getTime();
        timeElapsed.add(new TotalTime(startTime.toLocalDate(), duration));

        this.totalTimeRepository.saveAll(timeElapsed);
    }
}
