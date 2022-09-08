package com.example.Timer.service;

import com.example.Timer.exceptions.InvalidTimeIntervalException;
import com.example.Timer.exceptions.TimeIntervalOverlapException;
import com.example.Timer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimerService {
    @Autowired
    private final TotalTimeRepository totalTimeRepository;
    @Autowired
    private final TimeIntervalRepository timeIntervalRepository;

    public TimerService(TotalTimeRepository totalTimeRepository, TimeIntervalRepository timeIntervalRepository) {
        this.totalTimeRepository = totalTimeRepository;
        this.timeIntervalRepository = timeIntervalRepository;
    }

    public List<TotalTime> addInterval(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime))
            throw new InvalidTimeIntervalException();

        TimeInterval lastInterval = this.timeIntervalRepository.findTopByOrderByEndTimeDesc();
        LocalDateTime lastIntervalEndDateTime = LocalDateTime.of(lastInterval.getTimeIntervalKey().getDate(), lastInterval.getEndTime());

        if (!startTime.isAfter(lastIntervalEndDateTime))
            throw new TimeIntervalOverlapException();

        ArrayList<TotalTime> updatedTotalTime = new ArrayList<>();
        ArrayList<TimeInterval> timeIntervals = new ArrayList<>();
        long duration;

        while (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            LocalDateTime endStartTimeDay = startTime.withHour(23).withMinute(59).withSecond(59);
            timeIntervals.add(new TimeInterval(new TimeIntervalKey(startTime.toLocalDate(), startTime.toLocalTime()), endStartTimeDay.toLocalTime()));
            LocalDateTime startNextDay = startTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            duration = Duration.between(startTime, startNextDay).getSeconds();
            duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                    .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                    .getTime();
            updatedTotalTime.add(new TotalTime(startTime.toLocalDate(), duration));
            startTime = startNextDay;
        }

        timeIntervals.add(new TimeInterval(new TimeIntervalKey(startTime.toLocalDate(), startTime.toLocalTime()), endTime.toLocalTime()));
        duration = Duration.between(startTime, endTime).getSeconds();
        duration += this.totalTimeRepository.findById(startTime.toLocalDate())
                .orElse(new TotalTime(startTime.toLocalDate(), 0L))
                .getTime();
        updatedTotalTime.add(new TotalTime(startTime.toLocalDate(), duration));

        this.totalTimeRepository.saveAll(updatedTotalTime);
        this.timeIntervalRepository.saveAll(timeIntervals);
        return updatedTotalTime;
    }

    public TotalTime addOffset(LocalDate date, long timeOffSet) {
        TotalTime totalTime = this.totalTimeRepository.findById(date)
                .orElse(new TotalTime(date, 0L));
        totalTime.setTime(totalTime.getTime() + timeOffSet);
        this.totalTimeRepository.save(totalTime);
        return totalTime;
    }

    public TotalTime getTotalTime(LocalDate date) {
        return this.totalTimeRepository.findById(date)
                .orElse(new TotalTime(date, 0L));
    }

    public List<TimeInterval> getTimeIntervals(LocalDate date) {
        return this.timeIntervalRepository.findByTimeIntervalKeyDate(date);
    }
}
