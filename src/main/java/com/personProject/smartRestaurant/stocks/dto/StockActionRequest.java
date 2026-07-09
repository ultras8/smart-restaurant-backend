package com.personProject.smartRestaurant.stocks.dto;

import lombok.Data;

@Data
public class StockActionRequest {
    private Integer qty;
    private String unit;
    private String note;
}
