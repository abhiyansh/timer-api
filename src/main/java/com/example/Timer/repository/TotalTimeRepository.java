package com.example.Timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TotalTimeRepository extends JpaRepository<TotalTime, LocalDate> {
}
