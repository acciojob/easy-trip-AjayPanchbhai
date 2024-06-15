package com.driver.Repositories;

import com.driver.model.City;
import com.driver.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findByFromCityAndToCity(City fromCity, City toCity);
    List<Flight> findByFlightDate(Date flightDate);
    List<Flight> findByFlightDateAndFromCity(Date flightDate, City fromCity);
    List<Flight> findByFlightDateAndToCity(Date flightDate, City toCity);
}
