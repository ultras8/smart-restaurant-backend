package com.personProject.smartRestaurant.entities;

import com.personProject.smartRestaurant.enums.MenuStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "menus")
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private BigDecimal totalCost;

    @Column(nullable = false)
    private MenuStatus status = MenuStatus.AVAILABLE;

}
