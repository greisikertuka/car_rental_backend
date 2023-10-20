package com.car.rental.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity(name = "BOOKINGS")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "START_DATE")
    public Date startDate;

    @Column(name = "END_DATE")
    public Date endDate;

    @Column(name = "TIMESTAMP")
    public Date timeStamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RATING_ID", referencedColumnName = "ID")
    public Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}

