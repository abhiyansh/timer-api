package com.example.Timer.controller;

import com.example.Timer.TimerApplication;
import com.example.Timer.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TimerApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TimerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TotalTimeRepository totalTimeRepository;

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    @BeforeEach
    public void before() {
        totalTimeRepository.deleteAll();
        timeIntervalRepository.deleteAll();
    }

    @Test
    void shouldAddTimeIntervalSuccessfully() throws Exception {
        String timeIntervalRequest = "{" +
                "\"start\": \"2022-08-27T17:09:38\"," +
                "\"end\": \"2022-08-30T20:02:29\"" +
                "}";
        String expectedResponse = "[{\"date\":\"2022-08-27\",\"time\":24622}," +
                "{\"date\":\"2022-08-28\",\"time\":86400}," +
                "{\"date\":\"2022-08-29\",\"time\":86400}," +
                "{\"date\":\"2022-08-30\",\"time\":72149}]";


        mockMvc.perform(post("/time-intervals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeIntervalRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldReturnBadRequestWhenTimeIntervalIsInvalid() throws Exception {
        String timeIntervalRequest = "{" +
                "\"start\": \"2022-08-30T20:02:29\"," +
                "\"end\": \"2022-08-30T20:02:29\"" +
                "}";

        mockMvc.perform(post("/time-intervals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeIntervalRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictWhenGivenTimeIntervalOverlapsWithExistingInterval() throws Exception {
        timeIntervalRepository.save(new TimeInterval(new TimeIntervalKey(LocalDate.of(2022, 8, 27),
                LocalTime.of(17, 9, 38)), LocalTime.of(21, 4, 20)));
        String timeIntervalRequest = "{" +
                "\"start\": \"2022-08-27T20:09:38\"," +
                "\"end\": \"2022-08-30T20:02:29\"" +
                "}";

        mockMvc.perform(post("/time-intervals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeIntervalRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldAddTimeOffSetToADaySuccessfully() throws Exception {
        String date = "2022-08-27";
        long initTime = 100L;
        long timeOffSet = 24_622L;
        long finalTime = 24_722L;
        totalTimeRepository.save(new TotalTime(LocalDate.parse(date), initTime));
        String request = String.format("{\"date\":\"%S\",\"time\":%d}", date, timeOffSet);
        String expectedResponse = String.format("{\"date\":\"%S\",\"time\":%d}", date, finalTime);

        mockMvc.perform(post("/total-time/" + date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldReturnTotalTimeForADaySuccessfully() throws Exception {
        String date = "2022-08-27";
        long time = 100L;
        totalTimeRepository.save(new TotalTime(LocalDate.parse(date), time));
        String expectedResponse = String.format("{\"date\":\"%S\",\"time\":%d}", date, time);

        mockMvc.perform(get("/total-time/" + date))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldReturnTimeIntervalsCorrespondingToADateSuccessfully() throws Exception {
        String date = "2022-08-27";
        timeIntervalRepository.save(new TimeInterval(new TimeIntervalKey(LocalDate.parse(date),
                LocalTime.of(17, 9, 38)), LocalTime.of(21, 4, 20)));
        timeIntervalRepository.save(new TimeInterval(new TimeIntervalKey(LocalDate.parse(date),
                LocalTime.of(22, 9, 38)), LocalTime.of(23, 4, 20)));
        String expectedResponse = "[{\"start\":\"17:09:38\",\"end\":\"21:04:20\"}," +
                "{\"start\":\"22:09:38\",\"end\":\"23:04:20\"}]";

        mockMvc.perform(get("/time-intervals/" + date))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

}
