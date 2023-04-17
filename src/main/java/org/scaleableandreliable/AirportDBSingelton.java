package org.scaleableandreliable;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AirportDBSingelton {
  
  @Inject
  @DataSource("db1")
  AgroalDataSource ds;
  
  static AirportDBSingelton instance;
  // Will use this as a cache
  List<String> airports;

  public AirportDBSingelton() {
    this.airports = new ArrayList<>();
  }

  public List<String> RetrieveAirportsFromDB() {
    // TODO: add db connection
    return airports;
  }

  public void insertArrivals(Arrival ar) {
    String sql =
        "INSERT INTO ARRIVALS (icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign)"
            + ar.icao24
            + ar.firstSeen
            + ar.estDepartureAirport
            + ar.lastSeen
            + ar.estArrivalAirport
            + ar.callsign;
    try (Connection session = ds.getConnection()) {
      session.nativeSQL(sql);
      System.out.println("Add arrival for " + ar.icao24);
    } catch (SQLException e) {
      //TODO: Add some exception
    }
  }

  public static AirportDBSingelton getInstance() {
    if (instance == null) {
      instance = new AirportDBSingelton();
    }
    if (instance.airports.isEmpty()) {
      instance.RetrieveAirportsFromDB();
    }
    return instance;
  }
}
