package com.car_rental_system.car_Rental_system_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car_rental_system.car_Rental_system_be.dto.AuthenticationRequest;
import com.car_rental_system.car_Rental_system_be.dto.AuthenticationResponse;
import com.car_rental_system.car_Rental_system_be.dto.EmailRequestDto;
import com.car_rental_system.car_Rental_system_be.dto.OtpValidationRequestDto;
import com.car_rental_system.car_Rental_system_be.dto.PasswordUpdateRequestDto;
import com.car_rental_system.car_Rental_system_be.dto.SignupRequest;
import com.car_rental_system.car_Rental_system_be.dto.UserDto;
import com.car_rental_system.car_Rental_system_be.entity.User;
import com.car_rental_system.car_Rental_system_be.repository.UserRepository;
import com.car_rental_system.car_Rental_system_be.services.auth.AuthService;
import com.car_rental_system.car_Rental_system_be.services.auth.EmailService;
import com.car_rental_system.car_Rental_system_be.services.jwt.UserService;
import com.car_rental_system.car_Rental_system_be.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository; 
    private final EmailService emailService;

    private Map<String, String> otpStore = new HashMap<>();

    
    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest) {
        if (authService.hasCustomerWithEmail(signupRequest.getEmail()))
            return new ResponseEntity<>("Customer already exists", HttpStatus.NOT_ACCEPTABLE);

        UserDto createdCustomerDto = authService.createCustomer(signupRequest);

        if (createdCustomerDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException {
       System.out.println("Authcontroller--login()");
    	
    	try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("Incorrect Email Or Password.");
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());

        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        System.out.println("Authcontroller--before  jwtUtil generate token calling");

        final String jwt = jwtUtil.generateToken(userDetails);
        System.out.println("Authcontroller--after jwtUtil generate token calling");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody EmailRequestDto request) {
    	System.out.println("email-requestBody " +request.getEmail());
       Optional<User> userOpt = userRepository.findFirstByEmail(request.getEmail());
       System.out.println("userOpt " +userOpt);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOpt.get();
        String otp = String.format("%04d", new Random().nextInt(10000)); // Generate 4-digit OTP
 
		System.out.println("AuthController -sendotp method");
		System.out.println("otp:" +otp);
		System.out.println("user.getEmail()::" +user.getEmail());
		
		//Store OTP in the in-memory store, When the user refreshes the page:data will be lost after password will be update n then login page will be displayed.
		otpStore.put(user.getEmail(), otp);
		System.out.println("otpStore map" +otpStore);
        // Send OTP to user's email
        emailService.sendOtpEmail(user.getEmail(), otp);
        System.out.println("OTP sent successfully to: " + user.getEmail() + " with OTP: " + otp);
        return ResponseEntity.ok("OTP sent to email.");
    }
    
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody OtpValidationRequestDto request) {
    	System.out.println("AuthController: validateOtp method");
        String storedOtp = otpStore.get(request.getEmail());
        
        if (storedOtp == null) {
        	System.out.println("OTP not found for the given email");

            return ResponseEntity.badRequest().body("OTP not found for the given email.");
        }

        if (storedOtp.equals(request.getOtp())) {
        	System.out.println("OTP is valid");

            return ResponseEntity.ok("OTP is valid.");
        } else {
        	System.out.println("OTP doesn't match");

            return ResponseEntity.badRequest().body("OTP doesn't match.");
        }
    }
    
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePasswordAfterOtpValidation(@RequestBody PasswordUpdateRequestDto request) {
        System.out.println("AuthController: updatePasswordAfterOtpValidation method");

     
        // Step 3: Update password if OTP is valid
        Optional<User> userOpt = userRepository.findFirstByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOpt.get();
       System.out.println("user" +user);
        // Step 4: Set new password
        user.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));
        userRepository.save(user);

        System.out.println("Password updated successfully for user: " + user.getEmail());
        System.out.println("New password is: " + request.getNewPassword());

        return ResponseEntity.ok("Password updated successfully.");
    }

}
