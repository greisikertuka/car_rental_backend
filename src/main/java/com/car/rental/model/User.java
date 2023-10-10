package com.car.rental.model;

import com.car.rental.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    public Long id;

    @Column(name = "USERNAME")
    @NonNull
    public String username;

    @Column(name = "PASSWORD")
    @NonNull
    public String password;

    @Column(name = "ROLE")
    @NonNull
    public UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<Booking> bookings = new ArrayList<>();
}

