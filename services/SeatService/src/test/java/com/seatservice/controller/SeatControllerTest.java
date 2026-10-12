package com.seatservice.controller;

import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.security.jwt.JwtUtils;
import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.security.AuthTokenFilter;
import com.seatservice.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SeatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SeatService seatService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;

    private SeatResponse sampleSeat() {
        return SeatResponse.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .build();
    }

    @Test
    void getById_shouldReturnSuccess() throws Exception {
        when(seatService.getById(1L))
                .thenReturn(sampleSeat());

        mockMvc.perform(get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.seatNumber").value("12A"));

        verify(seatService).getById(1L);
    }

    @Test
    void getByFlightInstance_shouldReturnSeats() throws Exception {
        when(seatService.getByFlightInstance(100L))
                .thenReturn(List.of(sampleSeat()));

        mockMvc.perform(get("/api/seats/flight-instance/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].seatNumber").value("12A"));

        verify(seatService).getByFlightInstance(100L);
    }

    @Test
    void getAvailableSeats_shouldReturnAvailableSeats() throws Exception {
        when(seatService.getAvailableSeats(100L))
                .thenReturn(List.of(sampleSeat()));

        mockMvc.perform(get("/api/seats/flight-instance/100/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].seatStatus").value("AVAILABLE"));
    }


    @Test
    void getBySeatNumber_shouldReturnSeat() throws Exception {
        when(seatService.getBySeatNumber(100L, "12A"))
                .thenReturn(sampleSeat());

        mockMvc.perform(get("/api/seats/flight-instance/100/12A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.seatNumber").value("12A"));
    }

    @Test
    void getByBookingId_shouldReturnBookedSeats() throws Exception {
        SeatResponse bookedSeat = SeatResponse.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build();

        when(seatService.getByBookingId(500L))
                .thenReturn(List.of(bookedSeat));

        mockMvc.perform(get("/api/seats/booking/500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].bookingId").value(500))
                .andExpect(jsonPath("$.data[0].seatStatus").value("BOOKED"));
    }



}
