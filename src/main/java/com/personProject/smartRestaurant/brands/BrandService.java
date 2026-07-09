package com.personProject.smartRestaurant.brands;

import com.personProject.smartRestaurant.brands.dto.BrandRequest;
import com.personProject.smartRestaurant.brands.dto.BrandResponse;
import com.personProject.smartRestaurant.entities.Brand;
import com.personProject.smartRestaurant.entities.User;
import com.personProject.smartRestaurant.enums.RestaurantTypeEnums;
import com.personProject.smartRestaurant.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("ไม่พบข้อมูลผู้ใช้งานในระบบค่ะ"));
    }

    private BrandResponse convertToResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getType(),
                brand.getUser().getId(),
                brand.getUser().getUsername()
        );
    }

    @Transactional
    public BrandResponse createBrand(BrandRequest req) {

        User user = getCurrentUser();

        Brand brand = new Brand();
        brand.setName(req.getName().trim());
        brand.setUser(user);
        String type = req.getType().trim().toUpperCase();
        try {
            brand.setType(RestaurantTypeEnums.fromString(type));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ประเภทร้านค้าไม่ถูกต้อง");
        }
        return convertToResponse(brandRepository.save(brand));
    }

    @Transactional(readOnly = true)
    public BrandResponse getBrandById(UUID id) {
        User user = getCurrentUser();
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบแบรนด์ที่ระบุในระบบค่ะ"));

        if (!brand.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("คุณไม่มีสิทธิ์เข้าถึงข้อมูลแบรนด์นี้ค่ะ");
        }
        return convertToResponse(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        User user = getCurrentUser();
        List<Brand> brands = brandRepository.findByUserId(user.getId());
        return brands.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandResponse updateBrand(UUID id, BrandRequest req) {
        User user = getCurrentUser();
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบแบรนด์ที่ต้องการแก้ไขค่ะ"));

        if (!brand.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("คุณไม่มีสิทธิ์แก้ไขแบรนด์นี้ค่ะ");
        }

        brand.setName(req.getName().trim());
        String type = req.getType().trim().toUpperCase();
        try {
            brand.setType(RestaurantTypeEnums.fromString(type));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ประเภทร้านค้าไม่ถูกต้อง");
        }
        return convertToResponse(brandRepository.save(brand));
    }

    @Transactional
    public void deleteBrand(UUID id) {
        User user = getCurrentUser();
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบแบรนด์ที่ต้องการลบค่ะ"));

        if (!brand.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("คุณไม่มีสิทธิ์ลบแบรนด์นี้ค่ะ");
        }

        brandRepository.delete(brand);
    }

}
