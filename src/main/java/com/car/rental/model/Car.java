package com.car.rental.model;

import com.car.rental.model.enums.Brand;
import com.car.rental.model.enums.Color;
import com.car.rental.model.enums.FuelType;
import com.car.rental.model.enums.Transmission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
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
    @NonNull
    public Long id;

    @Column(name = "MODEL")
    @NonNull
    public String model;

    @Column(name = "BRAND")
    @NonNull
    @Enumerated(EnumType.STRING)
    public Brand brand;

    @Column(name = "ENGINE")
    @NonNull
    public String engine;

    @Column(name = "FUEL_TYPE")
    @NonNull
    @Enumerated(EnumType.STRING)
    public FuelType fuelType;

    @Column(name = "DOORS")
    @NonNull
    public int doors;

    @Column(name = "COLOR")
    @NonNull
    @Enumerated(EnumType.STRING)
    public Color color;

    @Column(name = "TRANSMISSION")
    @NonNull
    @Enumerated(EnumType.STRING)
    public Transmission transmission;

    @Column(name = "SEATS")
    @NonNull
    public int seats;

    @Column(name = "YEAR")
    @NonNull
    public int year;

    @Column(name = "LICENSE_PLATE")
    @NonNull
    public String licencePlate;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();
}

