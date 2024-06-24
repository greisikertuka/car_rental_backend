package com.car.rental.service;

import com.car.rental.model.Booking;
import com.car.rental.repository.BookingRepository;
import com.car.rental.repository.CarRepository;
import com.car.rental.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@ApplicationScoped
public class BookingService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getSimpleName());

    @Inject
    UserRepository userRepository;

    @Inject
    CarRepository carRepository;

    @Inject
    BookingRepository bookingRepository;

    public List<Booking> findBookingsByCarId(Long carId) {
        var car = carRepository.findById(carId);
        if (car == null) {
            LOGGER.warning("No bookings found");
            throw new NotFoundException();
        }
        LOGGER.info("Got bookings");
        return car.getBookings();
    }

    public List<Booking> findBookingsByUserId(Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            LOGGER.warning("No user found with id:" + userId);
            throw new NotFoundException();
        }
        user.setPassword("");
        LOGGER.info("Got bookings");
        return user.getBookings();
    }

    @Transactional
    public void insertBooking(Long userId, Long carId, Booking booking) {
        var user = userRepository.findById(userId);
        if (user == null) {
            LOGGER.warning("No user found with id:" + userId);
            throw new NotFoundException();
        }

        var car = carRepository.findById(carId);
        if (car == null) {
            LOGGER.warning("No car found with id:" + carId);
            throw new NotFoundException();
        }
        booking.setCar(car);
        user.getBookings().add(booking);
        userRepository.getEntityManager().merge(user);
    }

    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void updateBooking(Booking booking) {
        bookingRepository.getEntityManager().merge(booking);
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.listAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
}
