package com.snaptix.bookingservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long eventId;
    private String event;
    private Long capacity;
    private VenueResponse venueResponse;
    private BigDecimal ticketPrice;
}
