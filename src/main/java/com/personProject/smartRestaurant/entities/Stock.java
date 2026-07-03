package com.personProject.smartRestaurant.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="stock")
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer qty;

    private String unit;

    private BigDecimal cost;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    // ขั้นต่ำ เพื่อแสดงสถานะใกล้หมด
    private Integer min_threshold = 0;

}
