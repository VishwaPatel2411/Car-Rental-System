package com.car_rental_system.car_Rental_system_be.services.auth;

import com.car_rental_system.car_Rental_system_be.dto.SignupRequest;
import com.car_rental_system.car_Rental_system_be.dto.UserDto;

public interface AuthService {
    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
