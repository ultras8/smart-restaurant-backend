package com.personProject.smartRestaurant.stocks;

import com.personProject.smartRestaurant.brands.BrandRepository;
import com.personProject.smartRestaurant.entities.Brand;
import com.personProject.smartRestaurant.entities.Stock;
import com.personProject.smartRestaurant.entities.StockLog;
import com.personProject.smartRestaurant.entities.User;
import com.personProject.smartRestaurant.enums.StockAction;
import com.personProject.smartRestaurant.enums.StockStatus;
import com.personProject.smartRestaurant.enums.UserRole;
import com.personProject.smartRestaurant.stocks.dto.StockLogResponse;
import com.personProject.smartRestaurant.stocks.dto.StockReponse;
import com.personProject.smartRestaurant.stocks.dto.StockRequest;
import com.personProject.smartRestaurant.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockLogRepository stockLogRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    private Brand getCurrentBrand(UUID brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("ไม่พบแบรนด์นี้ค่ะ"));
    }

    private User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("ไม่พบข้อมูลผู้ใช้งานในระบบค่ะ"));
    }

    private  Stock getCurrentStock(UUID id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายการวัตถุดิบที่ต้องการลบค่ะ"));
    }

    private void validateUserAccessToBrand(User user, Brand brand) {
        boolean isOwner = brand.getUser().getId().equals(user.getId());
        boolean isEmployeeOfBrand = (user.getRole() == UserRole.EMPLOYEE || user.getRole() == UserRole.MANAGER)
                && user.getEmployee() != null
                && user.getEmployee().getBrand().getId().equals(brand.getId());

        if (!isOwner && !isEmployeeOfBrand) {
            throw new RuntimeException("คุณไม่มีสิทธิ์เข้าถึงข้อมูลสต็อกของแบรนด์นี้ค่ะ");
        }
    }

    private StockReponse convertToStockResponse(Stock stock, String note) {
        StockReponse response = new StockReponse();
        response.setId(stock.getId());
        response.setName(stock.getName());
        response.setQty(stock.getQty());
        response.setUnit(stock.getUnit());
        response.setCost(stock.getCost());
        response.setMin_threshold(stock.getMin_threshold());
        response.setNote(note);
        if (stock.getQty() <= stock.getMin_threshold()) {
            response.setStatus(StockStatus.LOW_STOCK);
        } else {
            response.setStatus(StockStatus.NORMAL);
        }
        return response;
    }

    @Transactional
    public StockReponse addStock(UUID brandId, StockRequest req) {
        User actor = getCurrentUser();
        Brand brand = getCurrentBrand(brandId);
        validateUserAccessToBrand(actor, brand);
        Optional<Stock> existingStock = stockRepository.findByBrandIdAndName(brandId, req.getName());

        Stock stockToSave;
        int qtyChanged;
        StockAction logAction;
        String logNote;

        if(existingStock.isPresent()) {
            stockToSave = existingStock.get();
            qtyChanged = req.getQty();
            stockToSave.setQty(stockToSave.getQty() + qtyChanged);
            if (req.getCost() != null) {
                stockToSave.setCost(req.getCost());
            }
            if (req.getMin_threshold() != null) {
                stockToSave.setMin_threshold(req.getMin_threshold());
            }
            logAction = StockAction.ADD_QTY;
            logNote = "เพิ่มจำนวนวัตถุดิบเข้าสู่รายการคลังสินค้า";
        }else{
            stockToSave = new Stock();
            stockToSave.setName(req.getName().trim());
            stockToSave.setQty(req.getQty());
            stockToSave.setUnit(req.getUnit());
            stockToSave.setCost(req.getCost());
            stockToSave.setMin_threshold(req.getMin_threshold() != null ? req.getMin_threshold() : 0);
            stockToSave.setBrand(brand);

            qtyChanged = req.getQty();
            logAction = StockAction.ADD_QTY;
            logNote = "เพิ่มรายการวัตถุดิบใหม่เข้าสู่คลังสินค้า";
        }
        Stock savedStock = stockRepository.save(stockToSave);
        StockLog log = new StockLog();
        log.setStock(savedStock);
        log.setQtyChanged(qtyChanged);
        log.setAction(logAction);
        log.setNote(logNote);
        log.setChangedBy(actor);
        stockLogRepository.save(log);

        return convertToStockResponse(savedStock, logNote);
    }

    @Transactional(readOnly = true)
    public List<StockReponse> getAllStocksByBrand(UUID brandId) {
        User actor = getCurrentUser();
        Brand brand = getCurrentBrand(brandId);
        validateUserAccessToBrand(actor, brand);
        return stockRepository.findByBrandId(brandId).stream()
                .map(stock -> convertToStockResponse(stock, "ดึงข้อมูลภาพรวมคลังสินค้า"))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StockReponse getStockById(UUID id) {
        User actor = getCurrentUser();
        Stock stock = getCurrentStock(id);
        validateUserAccessToBrand(actor, stock.getBrand());
        return convertToStockResponse(stock, "ดึงรายละเอียดวัตถุดิบรายชิ้น");
    }

    @Transactional(readOnly = true)
    public List<StockLogResponse> getStockLogs(UUID id) {
        User actor = getCurrentUser();
        Stock stock = getCurrentStock(id);
        validateUserAccessToBrand(actor, stock.getBrand());
        return stockLogRepository.findByStockIdOrderByCreatedAtDesc(id).stream()
                .map(log -> {
                    StockLogResponse res = new StockLogResponse();
                    res.setId(log.getId());
                    res.setQtyChanged(log.getQtyChanged());
                    res.setAction(log.getAction());
                    res.setNote(log.getNote());
                    res.setCreatedAt(log.getCreatedAt());
                    res.setChangedByUsername(log.getChangedBy().getUsername());
                    return res;
                }).collect(Collectors.toList());
    }

    @Transactional
    public StockReponse updateStock(UUID id, StockRequest req) {
        User actor = getCurrentUser();
        Stock stock = getCurrentStock(id);
        validateUserAccessToBrand(actor, stock.getBrand());
        stock.setName(req.getName().trim());
        stock.setUnit(req.getUnit());
        if (req.getCost() != null) stock.setCost(req.getCost());
        if (req.getMin_threshold() != null) stock.setMin_threshold(req.getMin_threshold());

        Stock updatedStock = stockRepository.save(stock);
        return convertToStockResponse(updatedStock, "แก้ไขรายละเอียดข้อมูลวัตถุดิบ");
    }
}
