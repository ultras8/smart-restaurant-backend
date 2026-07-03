package com.personProject.smartRestaurant.brands.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandRequest {
    @NotBlank(message = "กรุณาระบุชื่อร้านค้า")
    private String name;
    @NotBlank(message = "กรุณาระบุประเภทร้านค้า (เช่น READY_TO_EAT)")
    private String type;
}
