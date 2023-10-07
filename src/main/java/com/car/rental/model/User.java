package com.car.rental.model;

import com.car.rental.model.enums.UserRole;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

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
}

