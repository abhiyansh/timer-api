package com.example.Timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeIntervalRepository extends JpaRepository<TimeInterval, TimeIntervalKey> {
    TimeInterval findTopByOrderByEndTimeDesc();

    List<TimeInterval> findByDate(LocalDate date);
}
