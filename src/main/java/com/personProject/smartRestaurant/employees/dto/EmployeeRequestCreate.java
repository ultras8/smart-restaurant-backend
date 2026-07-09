package com.personProject.smartRestaurant.employees.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeRequestCreate {
    private String fname;
    private String lname;
    private UUID brandId;
}