package com.flightservice.elasticsearch.document;

import com.airlineportal.utils.Flight.FlightStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "flights")
@Setting(
        shards = 1,
        replicas = 0
)
public class FlightDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String flightNumber;

    @Field(type = FieldType.Long)
    private Long airlineId;

    @Field(type = FieldType.Long)
    private Long aircraftId;

    @Field(type = FieldType.Long)
    private Long departureAirportId;

    @Field(type = FieldType.Long)
    private Long arrivalAirportId;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime scheduledDeparture;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime scheduledArrival;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime actualDeparture;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime actualArrival;

    @Field(type = FieldType.Keyword)
    private FlightStatus flightStatus;

    @Field(type = FieldType.Boolean)
    private Boolean active;

    @Field(type = FieldType.Boolean)
    private Boolean delayed;

    @Field(type = FieldType.Long)
    private Long delayMinutes;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime searchableDeparture;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime searchableArrival;


}
