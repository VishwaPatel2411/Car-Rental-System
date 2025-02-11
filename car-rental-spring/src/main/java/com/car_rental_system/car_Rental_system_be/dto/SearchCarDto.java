package com.car_rental_system.car_Rental_system_be.dto;

import lombok.Data;

@Data
public class SearchCarDto {
    private String brand;
    private String type;
    private String transmission;
    private String color;
}
