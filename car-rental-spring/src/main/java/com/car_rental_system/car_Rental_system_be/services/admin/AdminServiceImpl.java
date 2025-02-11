package com.car_rental_system.car_Rental_system_be.services.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.car_rental_system.car_Rental_system_be.dto.BookACarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDtoListDto;
import com.car_rental_system.car_Rental_system_be.dto.SearchCarDto;
import com.car_rental_system.car_Rental_system_be.entity.BookACar;
import com.car_rental_system.car_Rental_system_be.entity.Car;
import com.car_rental_system.car_Rental_system_be.enums.BookCarStatus;
import com.car_rental_system.car_Rental_system_be.repository.BookACarRepository;
import com.car_rental_system.car_Rental_system_be.repository.CarRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CarRepository carRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {
    	System.out.println("admin service imple--post car");
        boolean isSuccessful = false;

        try {
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setDescription(carDto.getDescription());
            car.setPrice(carDto.getPrice());
            car.setTransmission(carDto.getTransmission());
            car.setType(carDto.getType());
            car.setYear(carDto.getYear());
            car.setImage(carDto.getImage().getBytes());

            carRepository.save(car);
        	System.out.println("admin service imple--post car 2");

            isSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        return carRepository.findById(id).map(Car::getCarDto).orElse(null); // map() is a method that applies a given function to each element of a stream
    }

    @Override
    public boolean updateCar(Long id, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();

            if (carDto.getImage() != null) {
                existingCar.setImage(carDto.getImage().getBytes());
            }

            existingCar.setPrice(carDto.getPrice());
            existingCar.setYear(carDto.getYear());
            existingCar.setType(carDto.getType());
            existingCar.setDescription(carDto.getDescription());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setColor(carDto.getColor());
            existingCar.setName(carDto.getName());
            existingCar.setBrand(carDto.getBrand());

            carRepository.save(existingCar);

            return true;
        }

        return false;
    }

    @Override
    public List<BookACarDto> getBookings() {
        return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long id, String status) {
        Optional<BookACar> optionalBookACar = bookACarRepository.findById(id);

        if (optionalBookACar.isPresent()) {
            BookACar bookACar = optionalBookACar.get();

            if (Objects.equals(status, "Approve")) {
                bookACar.setBookCarStatus(BookCarStatus.APPROVED);
            } else {
                bookACar.setBookCarStatus(BookCarStatus.REJECTED);
            }

            bookACarRepository.save(bookACar);

            return true;
        }

        return false;
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car = new Car();
        car.setBrand(searchCarDto.getBrand());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setColor(searchCarDto.getColor());

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Car> carExample = Example.of(car, exampleMatcher);

        List<Car> carList = carRepository.findAll(carExample);

        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));

        return carDtoListDto;
    }
}
