package com.personProject.smartRestaurant.stocks;

import com.personProject.smartRestaurant.entities.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockLogRepository extends JpaRepository<StockLog, UUID> {
    List<StockLog> findByStockIdOrderByCreatedAtDesc(UUID stockId);
}
