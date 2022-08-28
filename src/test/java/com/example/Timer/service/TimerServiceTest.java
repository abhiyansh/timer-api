package com.example.Timer.service;

import com.example.Timer.repository.TotalTime;
import com.example.Timer.repository.TotalTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TimerServiceTest {

    private TotalTimeRepository totalTimeRepository;

    @BeforeEach
    void setUp() {
        totalTimeRepository = mock(TotalTimeRepository.class);
        when(totalTimeRepository.findById(LocalDate.of(2022, 8, 27)))
                .thenReturn(Optional.of(new TotalTime(LocalDate.of(2022, 8, 27), 134L)));
        when(totalTimeRepository.findById(LocalDate.of(2022, 8, 28)))
                .thenReturn(Optional.empty());
        when(totalTimeRepository.findById(LocalDate.of(2022, 8, 29)))
                .thenReturn(Optional.empty());
        when(totalTimeRepository.findById(LocalDate.of(2022, 8, 30)))
                .thenReturn(Optional.empty());
    }

    @Test
    void shouldUpdateTotalTimeCorrespondingToEachDateWhenStartAndEndTimeAreGiven() {
        TimerService timerService = new TimerService(totalTimeRepository);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 17, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        List<TotalTime> expectedTotalTime = List.of(
                new TotalTime(LocalDate.of(2022, 8, 27), 24_756L),
                new TotalTime(LocalDate.of(2022, 8, 28), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 29), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 30), 72_149L)
        );

        timerService.addInterval(startTime, endTime);

        verify(totalTimeRepository).saveAll(expectedTotalTime);
    }

    @Test
    void shouldAddTimeOffsetToAGivenDate() {
        TimerService timerService = new TimerService(totalTimeRepository);
        LocalDate date = LocalDate.of(2022, 8, 27);
        long offsetInMinutes = 5L;
        TotalTime expectedTotalTime = new TotalTime(LocalDate.of(2022, 8, 27), 434);

        timerService.addOffset(date, offsetInMinutes);

        verify(totalTimeRepository).save(expectedTotalTime);
    }

    @Test
    void shouldReturnTotalTimeForADayIfItExists() {
        TimerService timerService = new TimerService(totalTimeRepository);
        LocalDate date = LocalDate.of(2022, 8, 27);
        long expectedTotalTime = 134L;

        long actualTotalTime = timerService.getTotalTime(date);

        assertEquals(expectedTotalTime, actualTotalTime);
    }

    @Test
    void shouldReturnTotalTimeForADayAsZeroIfItDoesNotExist() {
        TimerService timerService = new TimerService(totalTimeRepository);
        LocalDate date = LocalDate.of(2022, 8, 28);
        long expectedTotalTime = 0L;

        long actualTotalTime = timerService.getTotalTime(date);

        assertEquals(expectedTotalTime, actualTotalTime);
    }
}
