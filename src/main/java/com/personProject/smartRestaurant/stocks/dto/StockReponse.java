package com.personProject.smartRestaurant.stocks.dto;

import com.personProject.smartRestaurant.enums.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockReponse {
    private UUID id;
    private String name;
    private Integer qty;
    private String unit;
    private BigDecimal cost;
    private Integer min_threshold;
    private StockStatus status;
    private String note;
}
