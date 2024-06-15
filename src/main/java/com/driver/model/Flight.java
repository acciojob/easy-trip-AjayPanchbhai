package com.driver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id", nullable = false, unique = true)
    private Integer flightId; //This is a unique key for a flight

    @Enumerated(value = EnumType.STRING)
    @Column(name = "from_city")
    private City fromCity;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "to_city")
    private City toCity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "flight_date")
    private Date flightDate;

    @Column(name = "duration")
    private double duration;

    @OneToMany
    @JoinColumn(name = "passengers")
    @JsonBackReference
    List<Passenger> passengers;

    public Flight() {

    }

    public Flight(int flightId, City fromCity, City toCity, int maxCapacity, Date flightDate, double duration) {
        this.flightId = flightId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.maxCapacity = maxCapacity;
        this.flightDate = flightDate;
        this.duration = duration;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

}
