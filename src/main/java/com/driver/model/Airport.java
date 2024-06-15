package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @Column(name = "airport_name", nullable = false, unique = true)
    private String airportName; //This is the unique key

    @Column(name = "terminals")
    private Integer terminals;

    @Column(name = "city")
    @Enumerated(value = EnumType.STRING)
    private City city;  //GIVEN : There will be only 1 airport in 1 city


    public Airport(Integer terminals) {
        this.terminals = terminals;
    }

    public Airport(String airportName, Integer terminals, City city) {
        this.airportName = airportName;
        this.terminals = terminals;
        this.city = city;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public int getTerminals() {
        return terminals;
    }

    public void setTerminals(int terminals) {
        this.terminals = terminals;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
