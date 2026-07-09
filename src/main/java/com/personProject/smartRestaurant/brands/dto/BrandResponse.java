package com.personProject.smartRestaurant.brands.dto;

import com.personProject.smartRestaurant.enums.RestaurantTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private UUID id;
    private String name;
    private RestaurantTypeEnums type;
    private UUID userId;
    private String owner;
}
