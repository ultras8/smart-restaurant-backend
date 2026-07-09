package com.personProject.smartRestaurant.enums;

public enum MenuStatus {
    AVAILABLE,// พร้อมขาย / กำลังขายอยู่
    OUT_OF_STOCK,// ของหมดชั่วคราว (เช่น วันนี้หมูสับหมด หรือวัตถุดิบตามสูตรไม่พอตัดสต็อก)
    ARCHIVED; // เลิกขาย / ซ่อนเมนูไว้ก่อน (เช่น เมนูเทศกาลที่หมดช่วงไปแล้ว หรือขี้เกียจขายเมนูนี้แล้ว)
}
