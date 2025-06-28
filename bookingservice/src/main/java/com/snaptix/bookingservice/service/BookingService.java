package com.snaptix.bookingservice.service;

import com.snaptix.bookingservice.client.InventoryServiceClient;
import com.snaptix.bookingservice.entity.Customer;
import com.snaptix.bookingservice.repository.CustomerRepository;
import com.snaptix.bookingservice.request.BookingRequest;
import com.snaptix.bookingservice.response.BookingResponse;
import com.snaptix.bookingservice.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public BookingService(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
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
        System.out.println("Inventory Service Response " + inventoryResponse);

        // -- get event information to also get venue information
        // create booking
        // send booking to Order Service on a Kafka Topic
        return BookingResponse.builder().build();
    }
}
