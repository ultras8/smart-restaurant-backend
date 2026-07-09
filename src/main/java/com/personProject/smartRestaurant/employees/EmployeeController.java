package com.personProject.smartRestaurant.employees;

import com.personProject.smartRestaurant.employees.dto.EmployeeRequestCreate;
import com.personProject.smartRestaurant.employees.dto.EmployeeResponse;
import com.personProject.smartRestaurant.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequestCreate req) {
        return ResponseEntity.ok(employeeService.createEmployee(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeResponse> getEmployeeByCode(@PathVariable String employeeCode) {
        return ResponseEntity.ok(employeeService.getEmployeeByCode(employeeCode));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(@PathVariable UUID brandId) {
        return ResponseEntity.ok(employeeService.getAllEmployees(brandId));
    }

    @GetMapping("/brand/{brandId}/active")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeesActive(@PathVariable UUID brandId) {
        return ResponseEntity.ok(employeeService.getAllEmployeesActive(brandId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> updateEmployeeStatus(@PathVariable UUID id, @RequestParam UserStatus status) {
        return ResponseEntity.ok(employeeService.updateEmployeeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
