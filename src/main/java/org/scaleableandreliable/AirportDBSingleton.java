package org.scaleableandreliable;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AirportDBSingleton {

  private AgroalDataSource ds;

  private EntityManager em;

  static AirportDBSingleton instance;
  // Will use this as a cache
  List<String> airports;

  public AirportDBSingleton() {
    this.airports = new ArrayList<>();
  }

  void onApplicationStart(@Observes StartupEvent e) {
    System.out.println("Application started");

    System.out.println(ds);
    this.ds = ds;
  }

  public List<String> RetrieveAirportsFromDB() {
    // TODO: add db connection

    try (var session = ds.getConnection()) {
      String sql = "SELECT a.icao24 FROM Airports a";
      var statement = session.createStatement();
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        airports.add(resultSet.getString("icao24"));
      }
    } catch (SQLException e) {
      // TODO: Fixme
    }
    return airports;
  }

  public void insertArrivals(Arrivals ar) {

    if (ar.getId() == null) {
      ar.generateId();
    }

    String sql = getSql(ar);

    try (var session = ds.getConnection();
        var statement = session.createStatement(); ) {
      statement.execute(sql);
      System.out.println("Added arrival for " + ar.icao24);
    } catch (SQLException e) {
      System.out.println(e);
      // TODO: Add some exception
    }
  }

  private String getSql(Arrivals ar) {
    String sql =
        "INSERT INTO Arrivals (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount)"
            + " VALUES ("
            + ar.getId()
            + ","
            + "'"
            + ar.getIcao24()
            + "'"
            + ","
            + ar.getFirstSeen()
            + ","
            + "'"
            + ar.getEstDepartureAirport()
            + "'"
            + ","
            + ar.getLastSeen()
            + ","
            + "'"
            + ar.getEstArrivalAirport()
            + "'"
            + ","
            + "'"
            + ar.getCallsign()
            + "'"
            + ","
            + ar.getEstDepartureAirportHorizDistance()
            + ","
            + ar.getEstDepartureAirportVertDistance()
            + ","
            + ar.getEstArrivalAirportHorizDistance()
            + ","
            + ar.getEstArrivalAirportVertDistance()
            + ","
            + ar.getDepartureAirportCandidatesCount()
            + ","
            + ar.getArrivalAirportCandidatesCount()
            + ");";
    return sql;
  }

  public static AirportDBSingleton getInstance() {
    if (instance == null) {
      instance = new AirportDBSingleton();
    }
    if (instance.airports.isEmpty()) {
      //      instance.RetrieveAirportsFromDB();
    }
    return instance;
  }

  public AgroalDataSource getDs() {
    return ds;
  }

  public EntityManager getEm() {
    return em;
  }

  @Inject
  public AirportDBSingleton setDs(AgroalDataSource ds) {
    this.ds = ds;
    return this;
  }

  public AirportDBSingleton setEm(EntityManager em) {
    this.em = em;
    return this;
  }
}
