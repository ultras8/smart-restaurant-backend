package com.personProject.smartRestaurant.entities;

import com.personProject.smartRestaurant.enums.RestaurantTypeEnums;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "brands")
@Data
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantTypeEnums type;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
