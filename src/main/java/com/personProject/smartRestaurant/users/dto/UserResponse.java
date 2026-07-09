package com.personProject.smartRestaurant.users.dto;

import com.personProject.smartRestaurant.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String token;
    private UserRole role;
}
