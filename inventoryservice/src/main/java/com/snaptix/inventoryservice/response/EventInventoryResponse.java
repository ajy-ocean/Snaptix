package com.snaptix.inventoryservice.response;

import com.snaptix.inventoryservice.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.repository.NoRepositoryBean;

@Data
@Builder
@NoRepositoryBean
@AllArgsConstructor
public class EventInventoryResponse {

    private String event;
    private Long capacity;
    private Venue venue;
}
