package com.car_rental_system.car_Rental_system_be.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
