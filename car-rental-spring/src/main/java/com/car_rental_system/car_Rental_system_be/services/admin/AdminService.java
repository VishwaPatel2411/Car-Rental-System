package com.car_rental_system.car_Rental_system_be.services.admin;

import java.io.IOException;
import java.util.List;

import com.car_rental_system.car_Rental_system_be.dto.BookACarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDtoListDto;
import com.car_rental_system.car_Rental_system_be.dto.SearchCarDto;

public interface AdminService {
    boolean postCar(CarDto carDto) throws IOException;

    List<CarDto> getAllCars();

    void deleteCar(Long id);

    CarDto getCarById(Long id);

    boolean updateCar(Long id, CarDto carDto) throws IOException;

    List<BookACarDto> getBookings();


    boolean changeBookingStatus(Long id, String status);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);
}
