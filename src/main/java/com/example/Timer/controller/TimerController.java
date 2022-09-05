package com.example.Timer.controller;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TimerController {

    @Autowired
    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/time")
    public ResponseEntity<List<TotalTime>> addInterval(@RequestBody TimeIntervalRequest timeIntervalRequest) {
        List<TotalTime> totalTime = timerService.addInterval(timeIntervalRequest.getStartTime(), timeIntervalRequest.getEndTime());
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }

    @PostMapping("/time/{date}")
    public ResponseEntity<TotalTime> addOffset(@PathVariable String date, @RequestBody TimeOffSetRequest timeOffSetRequest) {
        TotalTime totalTime = timerService.addOffset(LocalDate.parse(date), timeOffSetRequest.getTimeOffSet());
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }

    @GetMapping("/time/{date}")
    public ResponseEntity<TotalTime> getTotal(@PathVariable String date) {
        TotalTime totalTime = timerService.getTotalTime(LocalDate.parse(date));
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }
}
