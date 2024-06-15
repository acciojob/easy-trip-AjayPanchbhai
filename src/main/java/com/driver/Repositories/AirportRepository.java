package com.driver.Repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, String> {
    Airport findByCity(City city);
    Airport findByAirportName(String airportName);
}
