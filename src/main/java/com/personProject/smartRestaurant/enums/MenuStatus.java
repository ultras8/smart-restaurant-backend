package com.personProject.smartRestaurant.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MenuStatus {
    AVAILABLE,// พร้อมขาย / กำลังขายอยู่
    OUT_OF_STOCK,// ของหมดชั่วคราว (เช่น วันนี้หมูสับหมด หรือวัตถุดิบตามสูตรไม่พอตัดสต็อก)
    ARCHIVED; // เลิกขาย / ซ่อนเมนูไว้ก่อน (เช่น เมนูเทศกาลที่หมดช่วงไปแล้ว หรือขี้เกียจขายเมนูนี้แล้ว)

    @JsonCreator
    public static MenuStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String cleanValue = value.trim().toUpperCase().replace("-", "_");

        try {
            return MenuStatus.valueOf(cleanValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ไม่พบสถานะเมนูชื่อ: " + value);
        }
    }
}
