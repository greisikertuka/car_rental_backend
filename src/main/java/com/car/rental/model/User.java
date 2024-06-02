package com.car.rental.model;

import com.car.rental.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
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
    public Long id;

    @Column(name = "NAME")
    public String name;

    @Column(name = "LAST_NAME")
    public String lastName;

    @Column(name = "EMAIL")
    public String email;

    @Column(name = "PHONE")
    public String phone;

    @Column(name = "USERNAME")
    public String username;

    @Column(name = "PASSWORD")
    public String password;

    @Column(name = "PROFILE_PICTURE_PATH")
    public String profilePicturePath;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    public Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
}

