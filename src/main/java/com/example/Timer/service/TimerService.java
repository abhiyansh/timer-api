package com.example.Timer.service;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.repository.TotalTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;

@Service
public class TimerService {
    private final TotalTimeRepository totalTimeRepository;

    @Autowired
    public TimerService(TotalTimeRepository totalTimeRepository) {
        this.totalTimeRepository = totalTimeRepository;
    }

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

    public void updateTotalTime(ArrayList<TotalTime> timeCorrespondingToDates) {
        for (TotalTime updatedTime : timeCorrespondingToDates) {
            TotalTime currentTotal = this.totalTimeRepository.findById(updatedTime.getDate())
                    .orElse(new TotalTime(updatedTime.getDate(), 0L));
            updatedTime.setTime(currentTotal.getTime() + updatedTime.getTime());
        }
        this.totalTimeRepository.saveAll(timeCorrespondingToDates);
    }
}
