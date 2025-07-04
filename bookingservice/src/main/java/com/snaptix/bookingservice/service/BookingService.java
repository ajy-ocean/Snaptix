package com.snaptix.bookingservice.service;

import com.snaptix.bookingservice.client.InventoryServiceClient;
import com.snaptix.bookingservice.entity.Customer;
import com.snaptix.bookingservice.event.BookingEvent;
import com.snaptix.bookingservice.repository.CustomerRepository;
import com.snaptix.bookingservice.request.BookingRequest;
import com.snaptix.bookingservice.response.BookingResponse;
import com.snaptix.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(CustomerRepository customerRepository,
                          InventoryServiceClient inventoryServiceClient,
                          final KafkaTemplate<String, BookingEvent> kafkaTemplate
                          ) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request){
        // check if the user exits
        final Customer customer = customerRepository.findById(request.getUserId()).orElse(null);
        if(customer == null){
            throw new RuntimeException("User Not Found!!");
        }
        // check if there is enough inventory
        final InventoryResponse inventoryResponse = inventoryServiceClient
                .getInventory(request.getEventId());
        log.info("Inventory Service Response " + inventoryResponse);

        if(inventoryResponse.getCapacity() < request.getTicketCount()){
            throw new RuntimeException("Not Enough Inventory");
        }

        // -- get event information to also get venue information
        // create booking
        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);

        // send booking to Order Service on a Kafka Topic
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking send to kafka: {}" + bookingEvent);
        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(
            final BookingRequest request,
            final Customer customer,
            final InventoryResponse inventoryResponse){
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                request.getTicketCount())))
                .build();
    }
}
