package com.car_rental_system.car_Rental_system_be.services.auth;

public interface EmailService {

	public void sendOtpEmail(String to, String otp);
}
