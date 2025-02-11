package com.car_rental_system.car_Rental_system_be.services.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.car_rental_system.car_Rental_system_be.dto.BookACarDto;
import com.car_rental_system.car_Rental_system_be.dto.CarDto;
import com.car_rental_system.car_Rental_system_be.entity.BookACar;
import com.car_rental_system.car_Rental_system_be.entity.Car;
import com.car_rental_system.car_Rental_system_be.entity.User;
import com.car_rental_system.car_Rental_system_be.enums.BookCarStatus;
import com.car_rental_system.car_Rental_system_be.repository.BookACarRepository;
import com.car_rental_system.car_Rental_system_be.repository.CarRepository;
import com.car_rental_system.car_Rental_system_be.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();

            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setBookCarStatus(BookCarStatus.PENDING);
            bookACar.setBrand(existingCar.getBrand());
            bookACar.setName(existingCar.getName());
            bookACar.setType(existingCar.getType());
           bookACar.setFromDate(bookACarDto.getFromDate());
           bookACar.setToDate(bookACarDto.getToDate());
            bookACar.setColor(existingCar.getColor());
            bookACar.setTransmission(existingCar.getTransmission());

            bookACar.setDays(bookACarDto.getDays());
            bookACar.setPrice(bookACarDto.getDays() * existingCar.getPrice());
            System.out.println("customer service");
            System.out.println(bookACar);
            bookACarRepository.save(bookACar);
            return true;
        }

        return false;
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
      //  return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
        List<BookACarDto> bookings = bookACarRepository.findAllByUserId(userId)
                .stream()
                .map(BookACar::getBookACarDto)
                .collect(Collectors.toList());
            
            System.out.println("Bookings Data: " + bookings); // Debugging Line
            return bookings;
    }
}
