package com.example.Timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeIntervalRepository extends JpaRepository<TimeInterval, TimeIntervalKey> {
    Optional<TimeInterval> findTopByOrderByEndTimeDesc();

    List<TimeInterval> findByTimeIntervalKeyDate(LocalDate date);
}
