package com.personProject.smartRestaurant.entities;

import com.personProject.smartRestaurant.enums.StockAction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_log")
@Data
public class StockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // quantity changed (example +10 or -5)
    @Column(name = "qty_changed")
    private Integer qtyChanged;

    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockAction action;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
