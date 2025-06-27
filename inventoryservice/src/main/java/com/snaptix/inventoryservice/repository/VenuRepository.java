package com.snaptix.inventoryservice.repository;

import com.snaptix.inventoryservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenuRepository extends JpaRepository<Venue, Long> {
}
