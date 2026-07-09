package com.personProject.smartRestaurant.stocks.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockRequest {
    @NotBlank(message = "กรุณาระบุชื่อสินค้า")
    private String name;
    @NotBlank(message = "กรุณาระบุจำนวน")
    private Integer qty;
    private String unit;
    private BigDecimal cost;
    private Integer min_threshold;
    private String note; //หมายเหตุ
}
