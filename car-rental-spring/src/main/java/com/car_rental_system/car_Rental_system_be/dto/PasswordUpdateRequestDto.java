package com.car_rental_system.car_Rental_system_be.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequestDto {
	 private String email;
	 private String newPassword;
}
