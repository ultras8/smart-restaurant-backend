package com.personProject.smartRestaurant.employees;

import com.personProject.smartRestaurant.brands.BrandRepository;
import com.personProject.smartRestaurant.employees.dto.EmployeeRequestCreate;
import com.personProject.smartRestaurant.employees.dto.EmployeeResponse;
import com.personProject.smartRestaurant.entities.Brand;
import com.personProject.smartRestaurant.entities.Employee;
import com.personProject.smartRestaurant.entities.User;
import com.personProject.smartRestaurant.enums.UserRole;
import com.personProject.smartRestaurant.enums.UserStatus;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("ไม่พบข้อมูลผู้ใช้งานในระบบค่ะ"));
    }

    private Brand getCurrentBrand(UUID brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("ไม่พบแบรนด์นี้ค่ะ"));
    }

    private String generateEmployeeCode() {
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "EMP" + randomPart;
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequestCreate req) {
        User actor = getCurrentUser();
        Brand brand = getCurrentBrand(req.getBrandId());

        boolean isOwner = brand.getUser().getId().equals(actor.getId());
        boolean isManager = actor.getRole() == UserRole.MANAGER &&
                actor.getEmployee() != null &&
                actor.getEmployee().getBrand().getId().equals(brand.getId());
        if (!isOwner && !isManager){
            throw new RuntimeException("คุณไม่มีสิทธิ์เข้าถึงค่ะ");
        }

        String employeeCode;
        do{
            employeeCode = generateEmployeeCode();
        }while (employeeRepository.findByEmployeeCode(employeeCode).isPresent());

        Employee profile = new Employee();
        profile.setEmployeeCode(employeeCode);
        profile.setFname(req.getFname());
        profile.setLname(req.getLname());
        profile.setStatus(UserStatus.ACTIVE);
        profile.setBrand(brand);

        Employee savedProfile = employeeRepository.save(profile);

        EmployeeResponse response = new EmployeeResponse();
        response.setId(savedProfile.getId());
        response.setEmployeeCode(savedProfile.getEmployeeCode());
        response.setFname(savedProfile.getFname());
        response.setLname(savedProfile.getLname());
        response.setStatus(savedProfile.getStatus().name());
        response.setBrandId(savedProfile.getBrand().getId());
        return response;
    }

    @Transactional
    public EmployeeResponse updateEmployeeStatus(UUID employeeId, UserStatus newStatus) {
        User actor = getCurrentUser();

        Employee profile = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน"));

        Brand brand = profile.getBrand();

        boolean isOwner = brand.getUser().getId().equals(actor.getId());
        boolean isManager = actor.getRole() == UserRole.MANAGER &&
                actor.getEmployee() != null &&
                actor.getEmployee().getBrand().getId().equals(brand.getId());
        if (!isOwner && !isManager){
            throw new RuntimeException("คุณไม่มีสิทธิ์เข้าถึงค่ะ");
        }

        profile.setStatus(newStatus);
        Employee updatedProfile = employeeRepository.save(profile);

        EmployeeResponse response = new EmployeeResponse();
        response.setId(updatedProfile.getId());
        response.setEmployeeCode(updatedProfile.getEmployeeCode());
        response.setFname(updatedProfile.getFname());
        response.setLname(updatedProfile.getLname());
        response.setStatus(updatedProfile.getStatus().name());
        response.setBrandId(updatedProfile.getBrand().getId());
        return response;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees(UUID brandId) {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .filter(employee -> employee.getBrand().getId().equals(brandId))
                .map( employee -> {
                    EmployeeResponse response = new EmployeeResponse();
                    response.setId(employee.getId());
                    response.setFname(employee.getFname());
                    response.setLname(employee.getLname());
                    response.setStatus(employee.getStatus().name());
                    return response;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployeesActive(UUID brandId) {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .filter(employee -> employee.getBrand().getId().equals(brandId) && employee.getStatus().equals(UserStatus.ACTIVE))
                .map( employee -> {
                    EmployeeResponse response = new EmployeeResponse();
                    response.setId(employee.getId());
                    response.setFname(employee.getFname());
                    response.setLname(employee.getLname());
                    response.setStatus(employee.getStatus().name());
                    return response;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(UUID id) {
        Employee profile = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน id: " + id));
        EmployeeResponse response = new EmployeeResponse();
        response.setId(profile.getId());
        response.setFname(profile.getFname());
        response.setLname(profile.getLname());
        response.setStatus(profile.getStatus().name());
        response.setBrandId(profile.getBrand().getId());
        return response;
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByCode(String code) {
        Employee profile = employeeRepository.findByEmployeeCode(code)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน รหัส: " + code));
        EmployeeResponse response = new EmployeeResponse();
        response.setId(profile.getId());
        response.setFname(profile.getFname());
        response.setLname(profile.getLname());
        response.setStatus(profile.getStatus().name());
        response.setBrandId(profile.getBrand().getId());
        return response;
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        User actor = getCurrentUser();
        Employee profile = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน id: " + id));

        Brand brand = profile.getBrand();

        boolean isOwner = brand.getUser().getId().equals(actor.getId());
        boolean isManager = actor.getRole() == UserRole.MANAGER &&
                actor.getEmployee() != null &&
                actor.getEmployee().getBrand().getId().equals(brand.getId());
        if (!isOwner && !isManager){
            throw new RuntimeException("คุณไม่มีสิทธิ์เข้าถึงค่ะ");
        }

        employeeRepository.deleteById(id);
    }
}
