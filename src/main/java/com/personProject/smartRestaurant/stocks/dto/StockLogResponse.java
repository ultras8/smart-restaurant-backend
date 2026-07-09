package com.personProject.smartRestaurant.stocks.dto;

import com.personProject.smartRestaurant.enums.StockAction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockLogResponse {
    private UUID id;
    private Integer qtyChanged;
    private StockAction action;
    private String note;
    private LocalDateTime createdAt;
    private String changedByUsername;
}
