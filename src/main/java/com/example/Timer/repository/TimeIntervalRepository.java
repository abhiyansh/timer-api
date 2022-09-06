package com.example.Timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeIntervalRepository extends JpaRepository<TimeInterval, TimeIntervalKey> {
    TimeInterval findTopByOrderByEndTimeDesc();
}
