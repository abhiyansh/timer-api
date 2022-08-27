package com.example.Timer.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimerServiceTest {
    @Test
    void shouldReturnTotalTimeCorrespondingToEachDayBetweenStartAndEndTime() {
        TimerService timerService = new TimerService();
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 17, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        TreeMap<LocalDate, Long> expectedTime = new TreeMap<>(Map.of(
                LocalDate.of(2022, 8, 27), 24_622L,
                LocalDate.of(2022, 8, 28), 86_400L,
                LocalDate.of(2022, 8, 29), 86_400L,
                LocalDate.of(2022, 8, 30), 72_149L
        ));

        TreeMap<LocalDate, Long> actualTime = timerService.timeElapsed(startTime, endTime);

        assertEquals(expectedTime, actualTime);
    }
}
