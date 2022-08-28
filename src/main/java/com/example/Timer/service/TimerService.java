package com.example.Timer.service;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.repository.TotalTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TimerService {
    private final TotalTimeRepository totalTimeRepository;

    @Autowired
    public TimerService(TotalTimeRepository totalTimeRepository) {
        this.totalTimeRepository = totalTimeRepository;
    }

    public void addInterval(LocalDateTime startTime, LocalDateTime endTime) {
        ArrayList<TotalTime> updatedTotalTime = new ArrayList<>();
        long duration;

        while (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            LocalDateTime startNextDay = startTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            duration = Duration.between(startTime, startNextDay).getSeconds();
            duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                    .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                    .getTime();
            updatedTotalTime.add(new TotalTime(startTime.toLocalDate(), duration));
            startTime = startNextDay;
        }

        duration = Duration.between(startTime, endTime).getSeconds();
        duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                .getTime();
        updatedTotalTime.add(new TotalTime(startTime.toLocalDate(), duration));

        this.totalTimeRepository.saveAll(updatedTotalTime);
    }

    public void addOffset(LocalDate date, long offsetInMinutes) {
        TotalTime timeCorrespondingToDate = this.totalTimeRepository.findById(date)
                .orElse(new TotalTime(date, 0L));
        timeCorrespondingToDate.setTime(timeCorrespondingToDate.getTime() + (offsetInMinutes * 60));
        this.totalTimeRepository.save(timeCorrespondingToDate);
    }

    public long getTotalTime(LocalDate date) {
        return this.totalTimeRepository.findById(date)
                .orElse(new TotalTime(date, 0L))
                .getTime();
    }
}
