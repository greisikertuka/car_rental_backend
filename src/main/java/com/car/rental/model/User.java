package com.car.rental.model;

import com.car.rental.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Enumerated(EnumType.STRING)
    public UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();
}

