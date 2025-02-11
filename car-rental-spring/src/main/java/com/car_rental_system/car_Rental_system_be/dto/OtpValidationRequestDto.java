package com.car_rental_system.car_Rental_system_be.dto;

public class OtpValidationRequestDto {

	 private String email;
	    private String otp;

	    // Getters and Setters
	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getOtp() {
	        return otp;
	    }

	    public void setOtp(String otp) {
	        this.otp = otp;
	    }
	}