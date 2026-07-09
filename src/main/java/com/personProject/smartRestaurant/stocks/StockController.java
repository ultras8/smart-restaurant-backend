package com.personProject.smartRestaurant.stocks;

import com.personProject.smartRestaurant.stocks.dto.StockLogResponse;
import com.personProject.smartRestaurant.stocks.dto.StockReponse;
import com.personProject.smartRestaurant.stocks.dto.StockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    @PostMapping("/brand/{brandId}")
    public ResponseEntity<StockReponse> addStock(@PathVariable UUID brandId, @RequestBody StockRequest req) {
        return ResponseEntity.ok(stockService.addStock(brandId, req));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<StockReponse>> getAllStocksByBrand(@PathVariable UUID brandId) {
        return ResponseEntity.ok(stockService.getAllStocksByBrand(brandId));
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<StockLogResponse>> getStockLogs(@PathVariable UUID id) {
        return ResponseEntity.ok(stockService.getStockLogs(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockReponse> getStockById(@PathVariable UUID id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockReponse> updateStock(@PathVariable UUID id, @RequestBody StockRequest req) {
        return ResponseEntity.ok(stockService.updateStock(id, req));
    }
}
