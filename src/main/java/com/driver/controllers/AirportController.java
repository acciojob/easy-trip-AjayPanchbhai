package com.driver.controllers;


import com.driver.Repositories.AirportRepository;
import com.driver.Repositories.FlightRepository;
import com.driver.Repositories.PassengerRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AirportController {
    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @PostMapping("/add_airport")
    public String addAirport(@RequestBody Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"
        airportRepository.save(airport);
        return "SUCCESS";
    }

    @GetMapping("/get-largest-aiport")
    public String getLargestAirportName(){

        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName
        List<Airport> airports = airportRepository.findAll();
        
        int maxi = 0;
        String terminal = "";
        for(Airport airport : airports) {
            int terminals = airport.getTerminals();
            if(terminals > maxi) {
                maxi = terminals;
                terminal = airport.getAirportName();
            } else if(terminals == maxi) {
                if(terminal.compareTo(airport.getAirportName()) < 0) {
                    terminal = airport.getAirportName();
                }
            }
        }

       return terminal;
    }

    @GetMapping("/get-shortest-time-travel-between-cities")
    public double getShortestDurationOfPossibleBetweenTwoCities(@RequestParam("fromCity") City fromCity, @RequestParam("toCity")City toCity){

        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.
        List<Flight> flights = flightRepository.findByFromCityAndToCity(fromCity, toCity);

        if(flights.isEmpty())
            return -1;

        OptionalDouble shortestDuration = flights.stream()
                .mapToDouble(Flight::getDuration)
                .min();

       return shortestDuration.orElse(-1);
    }

    @GetMapping("/get-number-of-people-on-airport-on/{date}")
    public int getNumberOfPeopleOn(@PathVariable("date") Date date,@RequestParam("airportName")String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight

        Airport airport = airportRepository.findByAirportName(airportName);

        City airportCity = airport.getCity();

        // Find all flights on the given date departing from or arriving at the airport city
        List<Flight> departingFlights = flightRepository.findByFlightDateAndFromCity(date, airportCity);
        List<Flight> arrivingFlights = flightRepository.findByFlightDateAndToCity(date, airportCity);

        // Calculate the total number of people
        int totalPeople = 0;
        for (Flight flight : departingFlights) {
            totalPeople += flight.getMaxCapacity();
        }
        for (Flight flight : arrivingFlights) {
            totalPeople += flight.getMaxCapacity();
        }

        return totalPeople;
    }

    @GetMapping("/calculate-fare")
    public int calculateFlightFare(@RequestParam("flightId")Integer flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        List<Flight> flights = flightRepository.findAll();
        int cnt = 0;

        for(Flight flight : flights) {
            if(flight.getFlightId() == flightId) {
                return 3000 + 2 * cnt;
            }

            cnt++;
        }

        return 0;
    }


    @PostMapping("/book-a-ticket")
    public String bookATicket(@RequestParam("flightId")Integer flightId,@RequestParam("passengerId")Integer passengerId){
        // Find the flight by its ID
        Flight flight = flightRepository.findById(flightId).orElse(null);

        // If the flight is not found, return "FAILURE"
        if (flight == null) {
            return "FAILURE";
        }

        List<Passenger> passengers = flight.getPassengers();

        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);

        if(passenger == null || passengers.size() == flight.getMaxCapacity())
            return "FAILURE";

        passenger.setFlight(flight);
        flight.getPassengers().add(passenger);

        flightRepository.save(flight);
        passengerRepository.save(passenger);

        return "SUCCESS";
    }

    @PutMapping("/cancel-a-ticket")
    public String cancelATicket(@RequestParam("flightId")Integer flightId,@RequestParam("passengerId")Integer passengerId){

        // If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        Flight flight = flightRepository.findById(flightId).orElse(null);
        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);

        if(flight == null || passenger == null || passenger.getFlight() == null)
            return "FAILURE";

        flight.getPassengers().remove(passenger);
        passenger.setFlight(null);

        flightRepository.save(flight);
        passengerRepository.save(passenger);

       return null;
    }


    @GetMapping("/get-count-of-bookings-done-by-a-passenger/{passengerId}")
    public int countOfBookingsDoneByPassengerAllCombined(@PathVariable("passengerId") Integer passengerId) {
        List<Flight> flights = flightRepository.findAll();
        int cnt = 0;
        for (Flight flight : flights) {
            for (Passenger passenger : flight.getPassengers()) {
                if (passenger.getPassengerId() == passengerId) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    @PostMapping("/add-flight")
    public String addFlight(@RequestBody Flight flight){

        //Return a "SUCCESS" message string after adding a flight.
        flightRepository.save(flight);
       return "SUCCESS";
    }


    @GetMapping("/get-aiportName-from-flight-takeoff/{flightId}")
    public String getAirportNameFromFlightId(@PathVariable("flightId")Integer flightId) {

        //We need to get the starting airportName from where the flight will be taking off
        // (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName
        Flight flight = flightRepository.findById(flightId).orElse(null);

        if(flight == null)
            return null;

        return flight.getFromCity().toString();
    }


    @GetMapping("/calculate-revenue-collected/{flightId}")
    public int calculateRevenueOfAFlight(@PathVariable("flightId")Integer flightId){

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        Flight flight = flightRepository.findById(flightId).orElse(null);
        int price = 0;

        for(int i = 0; i < Objects.requireNonNull(flight).getPassengers().size(); ++i) {
            price = price + 3000 + i * 50;
        }
        return price;
    }


    @PostMapping("/add-passenger")
    public String addPassenger(@RequestBody Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        passengerRepository.save(passenger);
       return "SUCCESS";
    }


}
