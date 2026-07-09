package com.personProject.smartRestaurant.enums;

public enum StockAction {
    CREATE,   // เพิ่มสินค้าชิ้นใหม่เข้าคลังครั้งแรก
    UPDATE,   // แก้ไขข้อมูลทั่วไป เช่น เปลี่ยนชื่อสินค้า
    ADD_QTY,  // เติมจำนวนสินค้า (เช่น รับของเข้าสต็อก)
    SUB_QTY,   // ลดจำนวนสินค้า (เช่น เบิกไปใช้ หรือขายออก)
}
