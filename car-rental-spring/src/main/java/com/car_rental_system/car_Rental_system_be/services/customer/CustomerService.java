package com.car_rental_system.car_Rental_system_be.services.customer;

import java.util.List;

import com.car_rental_system.car_Rental_system_be.dto.BookACarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDto;

public interface CustomerService {
    List<CarDto> getAllCars();

    boolean bookACar(BookACarDto bookACarDto);

    CarDto getCarById(Long id);

    List<BookACarDto> getBookingsByUserId(Long id);
}
