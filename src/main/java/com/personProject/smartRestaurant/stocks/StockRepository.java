package com.personProject.smartRestaurant.stocks;

import com.personProject.smartRestaurant.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    List<Stock> findByBrandId(UUID brandId);
    Optional<Stock> findByBrandIdAndName(UUID brandId, String name);
}
