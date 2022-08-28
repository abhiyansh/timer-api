package com.example.Timer.controller;

import com.example.Timer.service.TimerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TimerControllerTest {
    private TimerService timerService;

    @BeforeEach
    void setUp() {
        timerService = mock(TimerService.class);
    }

    @Test
    void shouldUpdateTotalTimeWhenStartAndEndTimeAreGiven() {
        TimerController timerController = new TimerController(timerService);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 17, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        TimeIntervalRequest timeIntervalRequest = new TimeIntervalRequest(startTime, endTime);

        timerController.addInterval(timeIntervalRequest);

        verify(timerService).addInterval(startTime, endTime);
    }

    @Test
    void shouldUpdateTotalTimeForADayWhenOffsetIsGiven() {
        TimerController timerController = new TimerController(timerService);
        String date = "2022-08-27";
        long timeOffSet = 5L;
        TimeOffSetRequest timeOffSetRequest = new TimeOffSetRequest(timeOffSet);

        timerController.addOffset(date, timeOffSetRequest);

        verify(timerService).addOffset(LocalDate.of(2022, 8, 27), timeOffSet);
    }

    @Test
    void shouldReturnTotalTimeForAGivenDate() {
        TimerController timerController = new TimerController(timerService);
        String date = "2022-08-27";
        long expectedTotal = 134L;
        when(timerService.getTotalTime(LocalDate.of(2022, 8, 27))).thenReturn(expectedTotal);

        Long actualTotal = timerController.getTotal(date).getBody();

        assertEquals(expectedTotal, actualTotal);
    }
}
