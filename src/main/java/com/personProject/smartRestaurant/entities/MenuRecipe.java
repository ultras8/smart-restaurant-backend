package com.personProject.smartRestaurant.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

// [เมนูกะเพราหมูสับ] [วัตถุดิบ: หมูสับ] [ใช้: 100 กรัม]
// [เมนูกะเพราหมูสับ] [วัตถุดิบ: ข้าวสวย] [ใช้: 1 จาน]
// [เมนูกะเพราปลาหมึก] [วัตถุดิบ: ปลาหมึก] [ใช้: 100 กรัม]
// [เมนูกะเพราปลาหมึก] [วัตถุดิบ: ข้าวสวย] [ใช้: 1 จาน]
@Entity
@Table(name = "menu_recipe")
@Data
public class MenuRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "qty_used", nullable = false)
    private Integer qtyUsed;

    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

}
