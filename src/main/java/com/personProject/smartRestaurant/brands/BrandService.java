package com.personProject.smartRestaurant.brands;

import com.personProject.smartRestaurant.brands.dto.BrandRequest;
import com.personProject.smartRestaurant.entities.Brand;
import com.personProject.smartRestaurant.enums.RestaurantTypeEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public Brand createBrand(BrandRequest req){

        Brand brand = new Brand();
        brand.setName(req.getName().trim());
        String type = req.getType().trim().toUpperCase();
        try {
            brand.setType(RestaurantTypeEnums.fromString(type));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ประเภทร้านค้าไม่ถูกต้อง");
        }

        return brandRepository.save(brand);
    }

}
