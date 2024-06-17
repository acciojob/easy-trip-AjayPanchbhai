package com.driver.Services;

import com.driver.Repositories.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {
    AirportRepository airportRepository = new AirportRepository();

    public String addairport(Airport airport){
        return airportRepository.addAirport(airport);
    }

    public String addFlight(Flight flight){
        String res = airportRepository.addflight(flight);
        return res;
    }

    public String addPassenger(Passenger passenger){
        String result = airportRepository.addPassenger(passenger);
        return result;
    }

    public String getLargestAirport(){
        ArrayList<Airport> list = new ArrayList<>();
        list = airportRepository.getallAirportslist();
        int mxterminals = 0;

        for(Airport airport : list){
            if(airport.getNoOfTerminals() > mxterminals) {
                mxterminals = airport.getNoOfTerminals();

            }
        }
        List<String> mxTerminalsAirportList = airportRepository.getAirportByTerminals(mxterminals);
        int n = mxTerminalsAirportList.size();
        if(mxterminals == 0){
            return "Max number of terminal is  zero";
        }
        if(n == 1){
            return mxTerminalsAirportList.get(0);
        }

        String ans =mxTerminalsAirportList.get(0);
        for (String airportName : mxTerminalsAirportList) {
            if (airportName.compareTo(ans) < 0) {
                ans = airportName;
            }
        }

        return ans;

    }

    public double getshortestdurationBtwcities(City fromCity, City toCity){
        ArrayList<Flight> list = new ArrayList<>();
        list = airportRepository.getallFlightslist();

        double shortestDuration = Integer.MAX_VALUE;
        for(Flight flight : list ){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity) ){
                if ( flight.getDuration() < shortestDuration) {
                    shortestDuration = flight.getDuration();
                }
            }
        }

        if(shortestDuration == Integer.MAX_VALUE){
            return -1;
        }
        return shortestDuration;
    }



    public String getAirportNameByFlightId(int flightId){
        ArrayList<Flight> flightslist = new ArrayList<>();
        flightslist = airportRepository.getallFlightslist();

        City anscity = null;
        for(Flight flight : flightslist){
            if(flight.getFlightId() == flightId){
                anscity = flight.getFromCity();
            }
        }
        ArrayList<Airport> airportslist = new ArrayList<>();
        airportslist = airportRepository.getallAirportslist();

        for(Airport airport : airportslist){
            if(airport.getCity() == anscity){
                return airport.getAirportName();
            }
        }
        return null;

    }


    public String bookTicket(int flightId, int passengerId){
        Flight flight = airportRepository.getFlightByFlightId(flightId);
        Passenger passenger = airportRepository.getPassengerByPassengerId(passengerId);

        if(flight == null || passenger == null) return "flight / passenger does not exist";

        int numberofticketsbookedforflightId = airportRepository.noOfTicketsBookedForFlightId(flightId);
        if(numberofticketsbookedforflightId >= flight.getMaxCapacity()){
            return "FAILURE";
        }

        return airportRepository.bookTicket(flightId, passengerId);

    }

    public int calculateFare(int flightId){
        int numberOfTicketsBookedForFlightId = airportRepository.noOfTicketsBookedForFlightId(flightId);
        int price = 3000 + numberOfTicketsBookedForFlightId * 50;

        return price;
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        int count = airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
        return count;
    }

    public String cancelATicket(int flightId, int passengerId){
        String result = airportRepository.cancelATicket(flightId, passengerId);
        return result;
    }

    public int calculateRevenueCollectedByFlight(int flightId){
        int price = calculateFare(flightId);
        int canceledTickes = airportRepository.getCancelTickets(flightId);
        int canceledPrice = canceledTickes * 50;
        return price - canceledPrice;
    }



    public int getNumberOfpeople(Date date, String airportname) {
        ArrayList<Airport> airportslist = new ArrayList<>();
        airportslist = airportRepository.getallAirportslist();

        City city = null;

        for (Airport airport : airportslist) {
            if (airport.getAirportName().equals(airportname)) {
                city = airport.getCity();
            }
        }

        ArrayList<Flight> flightslist = new ArrayList<>();
        flightslist = airportRepository.getallFlightslist();

        if (flightslist.isEmpty()) return 0;

        ArrayList<Flight> flights = new ArrayList<>();
        for (Flight flight : flightslist) {
            if (flight.getFlightDate().equals(date)) {
                flights.add(flight);
            }
        }
        int count = 0;
        for (Flight flight : flights) {
            count += airportRepository.noOfTicketsBookedForFlightId(flight.getFlightId());
        }

        return count;
    }
}
