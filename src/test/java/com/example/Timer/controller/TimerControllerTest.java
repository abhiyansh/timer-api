package com.example.Timer.controller;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.service.TimerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimerControllerTest {
    private TimerService timerService;

    @BeforeEach
    void setUp() {
        timerService = mock(TimerService.class);
    }

    @Test
    void shouldUpdateTotalTimeWhenDateTimeIntervalIsGiven() {
        TimerController timerController = new TimerController(timerService);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 17, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        DateTimeIntervalDTO dateTimeInterval = new DateTimeIntervalDTO(startTime, endTime);
        List<TotalTime> expectedUpdatedTotalTime = List.of(
                new TotalTime(LocalDate.of(2022, 8, 27), 24_756L),
                new TotalTime(LocalDate.of(2022, 8, 28), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 29), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 30), 72_149L)
        );
        when(timerService.addTimeInterval(startTime, endTime)).thenReturn(expectedUpdatedTotalTime);

        List<TotalTime> actualUpdatedTotalTime = timerController.addTimeInterval(dateTimeInterval).getBody();

        assertEquals(expectedUpdatedTotalTime, actualUpdatedTotalTime);
    }

    @Test
    void shouldUpdateTotalTimeForADayWhenTimeOffsetIsGiven() {
        TimerController timerController = new TimerController(timerService);
        String date = "2022-08-27";
        long timeOffSet = 5L;
        TotalTime timeOffSetRequest = new TotalTime(LocalDate.parse(date), timeOffSet);
        TotalTime expectedTotal = new TotalTime(LocalDate.parse(date), 300L);
        when(timerService.addTime(LocalDate.of(2022, 8, 27), timeOffSet)).thenReturn(expectedTotal);

        TotalTime actualTotal = timerController.addTime(date, timeOffSetRequest).getBody();

        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    void shouldReturnTotalTimeForAGivenDate() {
        TimerController timerController = new TimerController(timerService);
        String date = "2022-08-27";
        TotalTime expectedTotal = new TotalTime(LocalDate.parse(date), 134L);
        when(timerService.getTotalTime(LocalDate.of(2022, 8, 27))).thenReturn(expectedTotal);

        TotalTime actualTotal = timerController.getTotal(date).getBody();

        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    void shouldReturnTimeIntervalsForAGivenDate() {
        TimerController timerController = new TimerController(timerService);
        String date = "2022-08-27";
        List<TimeIntervalDTO> expectedTimeIntervals = List.of(
                new TimeIntervalDTO(LocalTime.of(17, 9, 38), LocalTime.of(17, 19, 59)),
                new TimeIntervalDTO(LocalTime.of(18, 0, 0), LocalTime.of(19, 30, 0)),
                new TimeIntervalDTO(LocalTime.of(23, 9, 38), LocalTime.of(23, 50, 59))
        );
        when(timerService.getTimeIntervals(LocalDate.of(2022, 8, 27))).thenReturn(expectedTimeIntervals);

        List<TimeIntervalDTO> actualTimeIntervals = timerController.getTimeIntervals(date).getBody();

        assertEquals(expectedTimeIntervals, actualTimeIntervals);
    }
}
