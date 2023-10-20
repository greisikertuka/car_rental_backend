package com.car.rental.service;

import com.car.rental.model.Rating;
import com.car.rental.model.User;
import com.car.rental.repository.BookingRepository;
import com.car.rental.repository.CarRepository;
import com.car.rental.repository.RatingRepository;
import com.car.rental.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Slf4j
@ApplicationScoped
public class RatingService {

    @Inject
    BookingRepository bookingRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CarRepository carRepository;

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> findRatingsByCarId(Long carId) {
        var car = carRepository.findById(carId);
        if (car == null) {
            throw new NotFoundException();
        }
        return car.getRatings();
    }

    public List<Rating> findRatingsByUserId(Long userId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        return user.getRatings();
    }

    @Transactional
    public void insertRating(Long bookingId, Long userId, Long carId, Rating rating) {
        User user;
        user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException();
        }

        var car = carRepository.findById(carId);
        if (car == null) {
            throw new NotFoundException();
        }

        var booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new NotFoundException();
        }
        booking.setRating(rating);
        booking.setUser(user);
        booking.setCar(car);
        bookingRepository.getEntityManager().merge(booking);
    }

    @Transactional
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Transactional
    public void updateRating(Rating rating) {
        ratingRepository.getEntityManager().merge(rating);
    }

    public List<Rating> findAllRatings() {
        return ratingRepository.listAll();
    }
}
