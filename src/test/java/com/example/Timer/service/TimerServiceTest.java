package com.example.Timer.service;

import com.example.Timer.exceptions.InvalidTimeIntervalException;
import com.example.Timer.exceptions.TimeIntervalOverlapException;
import com.example.Timer.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TimerServiceTest {

    private TotalTimeRepository totalTimeRepository;
    private TimeIntervalRepository timeIntervalRepository;

    @BeforeEach
    void setUp() {
        timeIntervalRepository = mock(TimeIntervalRepository.class);
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
        when(timeIntervalRepository.findTopByOrderByEndTimeDesc())
                .thenReturn((Optional.of(new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(10, 0, 0)), LocalTime.of(10, 2, 14)))));
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 17, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        List<TotalTime> expectedTotalTime = List.of(
                new TotalTime(LocalDate.of(2022, 8, 27), 24_756L),
                new TotalTime(LocalDate.of(2022, 8, 28), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 29), 86_400L),
                new TotalTime(LocalDate.of(2022, 8, 30), 72_149L)
        );
        List<TimeInterval> expectedTimeIntervals = List.of(
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(17, 9, 38)), LocalTime.of(23, 59, 59)),
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 28),
                        LocalTime.of(0, 0, 0)), LocalTime.of(23, 59, 59)),
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 29),
                        LocalTime.of(0, 0, 0)), LocalTime.of(23, 59, 59)),
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 30),
                        LocalTime.of(0, 0, 0)), LocalTime.of(20, 2, 29))
        );

        List<TotalTime> actualTotalTime = timerService.addTimeInterval(startTime, endTime);

        verify(totalTimeRepository).saveAll(expectedTotalTime);
        verify(timeIntervalRepository).saveAll(expectedTimeIntervals);
        assertEquals(expectedTotalTime, actualTotalTime);
    }

    @Test
    void shouldThrowTimeIntervalOverlapExceptionWhenCurrentIntervalOverlapsWithExistingInterval() {
        when(timeIntervalRepository.findTopByOrderByEndTimeDesc())
                .thenReturn(Optional.of(new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(17, 9, 38)), LocalTime.of(21, 4, 20))));
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 27, 20, 9, 38);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);

        assertThrows(TimeIntervalOverlapException.class, () -> timerService.addTimeInterval(startTime, endTime));
    }

    @Test
    void shouldThrowInvalidTimeIntervalExceptionWhenStartTimeIsEqualToEndTime() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);

        assertThrows(InvalidTimeIntervalException.class, () -> timerService.addTimeInterval(startTime, endTime));
    }

    @Test
    void shouldThrowInvalidTimeIntervalExceptionWhenStartTimeIsAfterEndTime() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDateTime startTime = LocalDateTime.of(2022, 8, 30, 21, 2, 29);
        LocalDateTime endTime = LocalDateTime.of(2022, 8, 30, 20, 2, 29);

        assertThrows(InvalidTimeIntervalException.class, () -> timerService.addTimeInterval(startTime, endTime));
    }

    @Test
    void shouldAddTimeOffsetToAGivenDate() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDate date = LocalDate.of(2022, 8, 27);
        long timeOffSet = 300L;
        TotalTime expectedTotalTime = new TotalTime(LocalDate.of(2022, 8, 27), 434L);

        TotalTime actualTotalTime = timerService.addTime(date, timeOffSet);

        verify(totalTimeRepository).save(expectedTotalTime);
        assertEquals(expectedTotalTime, actualTotalTime);
    }

    @Test
    void shouldReturnTotalTimeForADayIfItExists() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDate date = LocalDate.of(2022, 8, 27);
        TotalTime expectedTotalTime = new TotalTime(date, 134L);

        TotalTime actualTotalTime = timerService.getTotalTime(date);

        assertEquals(expectedTotalTime, actualTotalTime);
    }

    @Test
    void shouldReturnTotalTimeForADayAsZeroIfItDoesNotExist() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDate date = LocalDate.of(2022, 8, 28);
        TotalTime expectedTotalTime = new TotalTime(date, 0L);

        TotalTime actualTotalTime = timerService.getTotalTime(date);

        assertEquals(expectedTotalTime, actualTotalTime);
    }

    @Test
    void shouldReturnTimeIntervalsCorrespondingToADate() {
        TimerService timerService = new TimerService(totalTimeRepository, timeIntervalRepository);
        LocalDate date = LocalDate.of(2022, 8, 27);
        when(timeIntervalRepository.findByTimeIntervalKeyDate(LocalDate.of(2022, 8, 27)))
                .thenReturn(List.of(
                        new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                                LocalTime.of(17, 9, 38)), LocalTime.of(17, 19, 59)),
                        new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                                LocalTime.of(18, 0, 0)), LocalTime.of(19, 30, 0)),
                        new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                                LocalTime.of(23, 9, 38)), LocalTime.of(23, 50, 59))
                ));
        List<TimeInterval> expectedTimeIntervals = List.of(
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(17, 9, 38)), LocalTime.of(17, 19, 59)),
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(18, 0, 0)), LocalTime.of(19, 30, 0)),
                new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                        LocalTime.of(23, 9, 38)), LocalTime.of(23, 50, 59))
        );

        List<TimeInterval> actualTimeIntervals = timerService.getTimeIntervals(date);

        assertEquals(expectedTimeIntervals, actualTimeIntervals);
    }
}
