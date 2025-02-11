package com.car_rental_system.car_Rental_system_be.dto;

import lombok.Data;

import java.util.Date;

import com.car_rental_system.car_Rental_system_be.enums.BookCarStatus;

@Data
public class BookACarDto {
    private Long id;
    private Date fromDate;
    private Date toDate;
    private Long days;
    private Long price;
    private BookCarStatus bookCarStatus;
    private Long carId;
    private Long userId;
    private String username;
    private String email;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
}
