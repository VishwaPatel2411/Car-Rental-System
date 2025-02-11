package com.car_rental_system.car_Rental_system_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car_rental_system.car_Rental_system_be.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
