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

    @PostMapping("/time-intervals")
    public ResponseEntity<List<TotalTime>> addTimeInterval(@RequestBody DateTimeIntervalDTO dateTimeInterval) {
        List<TotalTime> totalTime = timerService.addTimeInterval(dateTimeInterval.getStart(), dateTimeInterval.getEnd());
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }

    @GetMapping("/time-intervals/{date}")
    public ResponseEntity<List<TimeIntervalDTO>> getTimeIntervals(@PathVariable String date) {
        List<TimeIntervalDTO> timeIntervals = timerService.getTimeIntervals(LocalDate.parse(date));
        return ResponseEntity.status(HttpStatus.OK).body(timeIntervals);
    }

    @PostMapping("/total-time/{date}")
    public ResponseEntity<TotalTime> addTime(@PathVariable String date, @RequestBody TotalTime timeOffSet) {
        TotalTime totalTime = timerService.addTime(LocalDate.parse(date), timeOffSet.getTime());
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }

    @GetMapping("/total-time/{date}")
    public ResponseEntity<TotalTime> getTotal(@PathVariable String date) {
        TotalTime totalTime = timerService.getTotalTime(LocalDate.parse(date));
        return ResponseEntity.status(HttpStatus.OK).body(totalTime);
    }
}
