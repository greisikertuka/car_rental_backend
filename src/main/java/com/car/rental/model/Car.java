package com.car.rental.model;

import com.car.rental.model.enums.Brand;
import com.car.rental.model.enums.Color;
import com.car.rental.model.enums.FuelType;
import com.car.rental.model.enums.Transmission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    public String model;

    @Column(name = "BRAND")
    @Enumerated(EnumType.STRING)
    @NotNull
    public Brand brand;

    @Column(name = "ENGINE")
    @NotNull
    public String engine;

    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    @NotNull
    public FuelType fuelType;

    @Column(name = "DOORS")
    @NotNull
    public int doors;

    @Column(name = "COLOR")
    @Enumerated(EnumType.STRING)
    @NotNull
    public Color color;

    @Column(name = "TRANSMISSION")
    @Enumerated(EnumType.STRING)
    @NotNull
    public Transmission transmission;

    @Column(name = "SEATS")
    @NotNull
    public int seats;

    @Column(name = "YEAR")
    @NotNull
    public int year;

    @Column(name = "LICENSE_PLATE")
    @NotNull
    public String licencePlate;

    @Column(name = "PRICE")
    @NotNull
    public double price;

    @Column(name = "AVERAGE_RATING")
    public double averageRating;

    @Column(name = "REVIEWS_COUNT")
    public int reviewsCount;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
}

