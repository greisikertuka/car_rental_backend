package com.car.rental.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "YEAR")
    @NonNull
    public int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Owner owner;
}
