package com.snaptix.inventoryservice.service;

import com.snaptix.inventoryservice.entity.Event;
import com.snaptix.inventoryservice.entity.Venue;
import com.snaptix.inventoryservice.repository.EventRepository;
import com.snaptix.inventoryservice.repository.VenuRepository;
import com.snaptix.inventoryservice.response.EventInventoryResponse;
import com.snaptix.inventoryservice.response.VenueInventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenuRepository venuRepository;

    @Autowired
    public InventoryService(EventRepository eventRepository, VenuRepository venuRepository) {
        this.eventRepository = eventRepository;
        this.venuRepository = venuRepository;
    }

    public List<EventInventoryResponse> getAllEvents(){
        final List<Event> events = eventRepository.findAll();

        return events.stream().map(event -> EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .build()).collect(Collectors.toList());
    }

    public VenueInventoryResponse getVenueInformation(final Long venueId){
        final Venue venue = venuRepository.findById(venueId).orElse(null);

        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

}
