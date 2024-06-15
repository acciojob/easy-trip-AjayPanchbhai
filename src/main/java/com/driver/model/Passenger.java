package com.driver.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id", nullable = false, unique = true)
    private Integer passengerId; //This is a unique key for Passenger model :

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "flight")
    @JsonManagedReference
    private Flight flight;

    public Passenger() {

    }

    public Flight getFlight() {
        return flight;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Passenger(int passengerId, String email, String name, int age) {
        this.passengerId = passengerId;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
