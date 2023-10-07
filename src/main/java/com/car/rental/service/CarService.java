package com.car.rental.service;

import com.car.rental.model.Car;
import com.car.rental.repository.CarRepository;
import com.car.rental.repository.OwnerRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Slf4j
@ApplicationScoped
public class CarService {

    @Inject
    CarRepository carRepository;

    @Inject
    OwnerRepository ownerRepository;

    public List<Car> findCarsByOwnerId(Long id) {
        var owner = ownerRepository.findById(id);
        if (owner == null) {
            throw new NotFoundException();
        }
        return owner.getCars();
    }

    @Transactional
    public void insertCar(Long id, Car car) {
        var owner = ownerRepository.findById(id);
        if (owner == null) {
            throw new NotFoundException();
        }
        owner.getCars().add(car);
        ownerRepository.getEntityManager().merge(owner);
    }

    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void update(Car car) {
        carRepository.getEntityManager().merge(car);
    }

    public List<Car> findAll() {
        return carRepository.listAll();
    }
}
