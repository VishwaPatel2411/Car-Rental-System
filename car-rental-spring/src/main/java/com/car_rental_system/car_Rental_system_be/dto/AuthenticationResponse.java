package com.car_rental_system.car_Rental_system_be.dto;

import com.car_rental_system.car_Rental_system_be.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private Long userId;
}
