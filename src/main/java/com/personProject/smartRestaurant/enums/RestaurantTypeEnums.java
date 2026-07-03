package com.personProject.smartRestaurant.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RestaurantTypeEnums {
    READY_TO_EAT, //ร้านสำเร็จ คาเฟ่ เบเกอรี่
    RAW_MATERIAL; // ร้านวัตถุดิบ คลัง ตลาดสด

    @JsonCreator
    public static RestaurantTypeEnums fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        // แปลงค่าที่รับมาเป็นตัวพิมพ์ใหญ่ และเปลี่ยนช่องว่าง/ขีดกลางให้เป็น Underline (_)
        String cleanValue = value.trim().toUpperCase().replace("-", "_");

        try {
            return RestaurantTypeEnums.valueOf(cleanValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ไม่พบประเภทร้านค้าชื่อ: " + value);
        }
    }
}