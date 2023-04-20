package org.scaleableandreliable;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

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

  @Inject Logger log;

  static AirportDBSingleton instance;
  // Will use this as a cache
  List<String> airports;

  public AirportDBSingleton() {
    this.airports = new ArrayList<>();
  }

  void onApplicationStart(@Observes StartupEvent e) {
    log.info("Application started");
  }

  public void retrieveAirportsFromDB() {

    try (var session = ds.getConnection();
        var statement = session.createStatement(); ) {
      String sql = "SELECT a.icao24 FROM Airports a";
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        airports.add(resultSet.getString("icao24"));
      }
    } catch (SQLException e) {
      log.error("Got an error while retrieving airports from db", e);
    }
  }

  public void insertArrivals(Arrivals ar) {

    if (ar.getId() == null) {
      ar.generateId();
    }

    String sql = getSql(ar);

    try (var session = ds.getConnection();
        var statement = session.createStatement(); ) {
      statement.execute(sql);
      log.info("Added arrivals for airport" + ar.icao24);
    } catch (SQLException e) {
      log.error("Got an error while inserting arrivals", e);
    }
  }

  private String getSql(Arrivals ar) {
    return "INSERT INTO Arrivals (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount)"
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
