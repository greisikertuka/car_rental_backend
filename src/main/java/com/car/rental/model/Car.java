package com.car.rental.model;

import com.car.rental.model.enums.Brand;
import com.car.rental.model.enums.Color;
import com.car.rental.model.enums.FuelType;
import com.car.rental.model.enums.Transmission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "CARS")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "MODEL")
    public String model;

    @Column(name = "BRAND")
    @Enumerated(EnumType.STRING)
    public Brand brand;

    @Column(name = "ENGINE")
    public String engine;

    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    public FuelType fuelType;

    @Column(name = "DOORS")
    public int doors;

    @Column(name = "COLOR")
    @Enumerated(EnumType.STRING)
    public Color color;

    @Column(name = "TRANSMISSION")
    @Enumerated(EnumType.STRING)
    public Transmission transmission;

    @Column(name = "SEATS")
    public int seats;

    @Column(name = "YEAR")
    public int year;

    @Column(name = "LICENSE_PLATE")
    public String licencePlate;

    @Column(name = "PRICE")
    public double price;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
}

