package com.seatservice.service;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.external.ExternalService;
import com.seatservice.helper.SeatHelper;
import com.seatservice.model.Seat;
import com.seatservice.repository.SeatRepository;
import com.seatservice.service.Impl.SeatServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.junit.jupiter.api.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceImplTest {
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatHelper seatHelper;

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private SeatServiceImpl seatService;

    private Seat seat;

    @BeforeEach
    void setUp() {
        seat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .bookingId(null)
                .build();
    }

    @Test
    void createSeat_shouldCreateSeatWhenFlightInstanceExists() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("12A");
        when(request.getSeatClass()).thenReturn(SeatClass.ECONOMY);

        ApiResponse<FlightInstanceResponse> flightResponse = mock(ApiResponse.class);
        when(flightResponse.getData()).thenReturn(
                mock(FlightInstanceResponse.class)
        );

        when(externalService.getInstanceById(100L))
                .thenReturn(flightResponse);

        when(seatRepository.save(any(Seat.class)))
                .thenAnswer(invocation -> {
                   Seat saved = invocation.getArgument(0);
                   saved.setId(1L);
                   return saved;
                });

        SeatResponse result = seatService.createSeat(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getFlightInstanceId());
        assertEquals("12A", result.getSeatNumber());
        assertEquals(SeatClass.ECONOMY, result.getSeatClass());
        assertEquals(SeatStatus.AVAILABLE, result.getSeatStatus());

        verify(seatHelper).validateCreate(request);
        verify(externalService).getInstanceById(100L);
        verify(seatRepository).save(any(Seat.class));

    }

    @Test
    void createSeat_shouldThrowWhenFlightInstanceDoesNotExist() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(999L);

        ApiResponse<FlightInstanceResponse> flightResponse = mock(ApiResponse.class);
        when(flightResponse.getData()).thenReturn(null);

        when(externalService.getInstanceById(999L))
                .thenReturn(flightResponse);

        assertThrows(
                ResourceNotFoundException.class,
                () -> seatService.createSeat(request)
        );

        verify(seatRepository, never()).save(any(Seat.class));
    }

    @Test
    void createSeat_shouldThrowWhenFlightResponseIsNull() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(999L);
        when(externalService.getInstanceById(999L)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> seatService.createSeat(request)
        );

        verify(seatRepository, never()).save(any(Seat.class));
    }

    @Test
    void getById_shouldReturnSeatResponse() {
        when(seatHelper.findById(1L)).thenReturn(seat);

        SeatResponse result = seatService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12A", result.getSeatNumber());

        verify(seatHelper).findById(1L);
    }

    @Test
    void getBySeatNumber_shouldReturnSeatResponse() {
        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(seat);

        SeatResponse result = seatService.getBySeatNumber(100L, "12A");

        assertNotNull(result);
        assertEquals("12A", result.getSeatNumber());

        verify(seatHelper)
                .findByInstanceAndNumber(100L, "12A");
    }

    @Test
    void getByFlightInstance_shouldReturnSeats() {
        Seat secondSecond = Seat.builder()
                .id(2L)
                .flightInstanceId(100L)
                .seatNumber("12B")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .build();

        when(seatRepository.findByFlightInstanceId(100L))
                .thenReturn(List.of(seat, secondSecond));

        List<SeatResponse> result = seatService.getByFlightInstance(100L);
        assertEquals(2, result.size());
        assertEquals("12A", result.get(0).getSeatNumber());
        assertEquals("12B", result.get(1).getSeatNumber());
    }


    @Test
    void getByFlightInstance_shouldThrowWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.getByFlightInstance(null)
        );

        verifyNoInteractions(seatRepository);
    }


    @Test
    void getAvailableSeats_shouldReturnOnlyAvailableSeats() {
        when(seatRepository.findByFlightInstanceIdAndSeatStatus(100L, SeatStatus.AVAILABLE))
                .thenReturn(List.of(seat));

        List<SeatResponse> result = seatService.getAvailableSeats(100L);

        assertEquals(1, result.size());
        assertEquals(SeatStatus.AVAILABLE, result.getFirst().getSeatStatus());

        verify(seatRepository).findByFlightInstanceIdAndSeatStatus(100L, SeatStatus.AVAILABLE);
    }

    @Test
    void getAvailableSeats_shouldThrowWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.getAvailableSeats(null)
        );
    }


    @Test
    void bookSeat_shouldBookAvailableSeat() {
        when(seatHelper.normalizeSeatNumber("12a"))
                .thenReturn("12A");

        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(seat);

        when(seatRepository.bookSeat(
            100L,
            "12A",
            500L,
            SeatStatus.BOOKED,
            SeatStatus.AVAILABLE
        )).thenReturn(1);

        Seat bookedSeat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build();

        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(seat, bookedSeat);

        SeatResponse result = seatService.bookSeat(100L, "12a", 500L);

        assertNotNull(result);
        assertEquals(SeatStatus.BOOKED, result.getSeatStatus());
        assertEquals(500L, result.getBookingId());

        verify(seatHelper).validateAvailable(seat);
        verify(seatRepository).bookSeat(
                100L, "12A", 500L,
                SeatStatus.BOOKED, SeatStatus.AVAILABLE
        );

    }

    @Test
    void bookSeat_shouldThrowWhenFlightInstanceIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.bookSeat(null, "12A", 500L)
        );

        verifyNoInteractions(seatRepository);
    }

    @Test
    void bookSeat_shouldThrowWhenBookingIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.bookSeat(100L, "12A", null)
        );

        verifyNoInteractions(seatRepository);
    }

    @Test
    void bookSeat_shouldThrowWhenUpdateAffectsNoRows() {
        when(seatHelper.normalizeSeatNumber("12A"))
                .thenReturn("12A");
        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(seat);

        when(seatRepository.bookSeat(
                100L, "12A", 500L,
                SeatStatus.BOOKED, SeatStatus.AVAILABLE
        )).thenReturn(0);

        assertThrows(
                IllegalStateException.class,
                () -> seatService.bookSeat(100L, "12A", 500L)
        );

    }


    @Test
    void releaseSeat_shouldReleaseBookedSeat() {
        Seat bookedSeat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build();

        when(seatHelper.normalizeSeatNumber("12A"))
                .thenReturn("12A");

        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(bookedSeat, seat);

        when(seatRepository.releaseSeat(
                100L,
                "12A",
                SeatStatus.AVAILABLE,
                SeatStatus.BOOKED
        )).thenReturn(1);

        SeatResponse result = seatService.releaseSeat(100L, "12A");
        assertNotNull(result);
        assertEquals(SeatStatus.AVAILABLE, result.getSeatStatus());

        verify(seatRepository).releaseSeat(100L, "12A", SeatStatus.AVAILABLE, SeatStatus.BOOKED);

    }

    @Test
    void releaseSeat_shouldThrowWhenFlightInstanceIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.releaseSeat(null, "12A")
        );
    }

    @Test
    void releaseSeat_shouldThrowWhenUpdateAffectsNoRows() {
        when(seatHelper.normalizeSeatNumber("12A"))
                .thenReturn("12A");
        when(seatHelper.findByInstanceAndNumber(100L, "12A"))
                .thenReturn(seat);

        when(seatRepository.releaseSeat(
                100L, "12A",
                SeatStatus.AVAILABLE, SeatStatus.BOOKED
        )).thenReturn(0);

        assertThrows(
                IllegalStateException.class,
                () -> seatService.releaseSeat(100L, "12A")
        );

    }


    @Test
    void getByBookingId_shouldReturnSeats() {
        Seat bookedSeat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build();

        when(seatRepository.findByBookingId(500L))
                .thenReturn(List.of(bookedSeat));

        List<SeatResponse> result = seatService.getByBookingId(500L);
        assertEquals(1, result.size());
        assertEquals(500L, result.getFirst().getBookingId());
    }

    @Test
    void getByBookingId_shouldThrowWhenBookingIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatService.getByBookingId(null)
        );
    }




}
