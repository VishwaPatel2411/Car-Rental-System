package com.car_rental_system.car_Rental_system_be.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car_rental_system.car_Rental_system_be.repository.BookACarRepository;
import com.car_rental_system.car_Rental_system_be.repository.CarRepository;
import com.car_rental_system.car_Rental_system_be.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
        mailSender.send(message);
    }
}
