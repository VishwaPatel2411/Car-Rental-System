package com.car_rental_system.car_Rental_system_be.dto;

import com.car_rental_system.car_Rental_system_be.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
}
