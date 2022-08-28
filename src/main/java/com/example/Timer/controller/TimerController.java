package com.example.Timer.controller;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class TimerController {

    @Autowired
    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/timer")
    public void addInterval(@RequestBody TimeIntervalRequest timeIntervalRequest) {
        timerService.addInterval(timeIntervalRequest.getStartTime(), timeIntervalRequest.getEndTime());
    }

    @PostMapping("/timer/{date}")
    public void addOffset(@PathVariable String date, @RequestBody TimeOffSetRequest timeOffSetRequest) {
        timerService.addOffset(LocalDate.parse(date), timeOffSetRequest.getTimeOffSet());
    }

    @GetMapping("/timer/{date}")
    public ResponseEntity<TotalTime> getTotal(@PathVariable String date) {
        TotalTime totalTime = timerService.getTotalTime(LocalDate.parse(date));
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }
}
