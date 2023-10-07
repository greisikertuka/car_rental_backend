package com.car.rental.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "OWNERS")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    public Long id;

    @Column(name = "FIRST_NAME")
    @NonNull
    public String firstName;

    @Column(name = "LAST_NAME")
    @NonNull
    public String lastName;

    @Column(name = "PHONE_NUMBER")
    @NonNull
    public String phoneNumber;

    @Column(name = "AGE")
    @NonNull
    public int age;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("owner")
    private List<Car> cars = new ArrayList<>();
}
