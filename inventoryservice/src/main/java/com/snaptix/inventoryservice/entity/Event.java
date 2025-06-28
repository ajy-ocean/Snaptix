package com.snaptix.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="event")
public class Event {

    @Id
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="total_capacity")
    private Long totalCapacity;

    @Column(name="left_capacity")
    private Long leftCapacity;

    @ManyToOne
    @JoinColumn(name="venue_id")
    private Venue venue;

    @Column(name="ticket_price")
    private BigDecimal ticketPrice;
}
