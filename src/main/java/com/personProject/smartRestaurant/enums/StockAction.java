package com.personProject.smartRestaurant.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StockAction {
    CREATE,   // เพิ่มสินค้าชิ้นใหม่เข้าคลังครั้งแรก
    UPDATE,   // แก้ไขข้อมูลทั่วไป เช่น เปลี่ยนชื่อสินค้า
    ADD_QTY,  // เติมจำนวนสินค้า (เช่น รับของเข้าสต็อก)
    SUB_QTY;   // ลดจำนวนสินค้า (เช่น เบิกไปใช้ หรือขายออก)

    @JsonCreator
    public static StockAction fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null; // หรือจะ throw exception ก็ได้ค่ะ
        }

        // แปลงค่าที่รับมาเป็นตัวพิมพ์ใหญ่ และเปลี่ยนช่องว่าง/ขีดกลางให้เป็น Underline (_)
        String cleanValue = value.trim().toUpperCase().replace("-", "_");

        try {
            return StockAction.valueOf(cleanValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ไม่พบประเภทร้านค้าชื่อ: " + value);
        }
    }
}
