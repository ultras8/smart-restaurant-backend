package com.personProject.smartRestaurant.brands;

import com.personProject.smartRestaurant.brands.dto.BrandRequest;
import com.personProject.smartRestaurant.brands.dto.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@RequestBody BrandRequest req) {
        return  ResponseEntity.ok(brandService.createBrand(req));
    }

    @GetMapping
    public  ResponseEntity<List<BrandResponse>> getAllBrands() {
        return  ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable UUID id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable UUID id, @RequestBody BrandRequest req) {
        return ResponseEntity.ok(brandService.updateBrand(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable UUID id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete success");
    }

}
