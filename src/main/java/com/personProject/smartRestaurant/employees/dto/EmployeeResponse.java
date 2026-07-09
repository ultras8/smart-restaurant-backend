package com.personProject.smartRestaurant.employees.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeResponse {
    private UUID id;
    private String employeeCode;
    private String fname;
    private String lname;
    private String status;
    private UUID brandId;
}
