package com.car.rental.service;

import com.car.rental.model.Car;
import com.car.rental.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@ApplicationScoped
public class CarService {

    @Inject
    CarRepository carRepository;


    @Transactional
    public void insertCar(Car car) {
        carRepository.persist(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void updateCar(Car car) {
        carRepository.getEntityManager().merge(car);
    }

    public List<Car> findAllCars() {
        return carRepository.listAll();
    }
}
