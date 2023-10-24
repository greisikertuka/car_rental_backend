package com.car.rental.model;

import com.car.rental.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "NAME")
    @NotNull
    public String name;

    @Column(name = "LAST_NAME")
    @NotNull
    public String lastName;

    @Column(name = "EMAIL")
    @NotNull
    public String email;

    @Column(name = "PHONE")
    @NotNull
    public String phone;

    @Column(name = "USERNAME")
    @NotNull
    public String username;

    @Column(name = "PASSWORD")
    @NotNull
    public String password;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    @NotNull
    public UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
}

