package org.scaleableandreliable.DBhandlers;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import org.scaleableandreliable.models.Arrivals;
import org.scaleableandreliable.models.Coordinates;
import org.scaleableandreliable.models.GenericArDep;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DBSingleton {

  static DBSingleton instance;
  @Inject Logger log;
  // Will use this as a cache
  List<String> airports;
  List<Coordinates> coordinates;

  private AgroalDataSource ds;
  private EntityManager em;

  public DBSingleton() {
    this.airports = new ArrayList<>();
    this.coordinates = new ArrayList<>();
  }

  public static DBSingleton getInstance() {
    return instance;
  }

  public static void setInstance(DBSingleton instance) {
    DBSingleton.instance = instance;
  }

  void onApplicationStart(@Observes StartupEvent e) {
    log.info("Application started");
    retrieveAirportsFromDB();
    retrieveCoordinatesFromDB();
  }

  public void retrieveAirportsFromDB() {

    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      String sql = "SELECT a.icao24 FROM Airports a";
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        airports.add(resultSet.getString("icao24"));
        log.info("Retrieved airport icao24 from database");
      }
    } catch (SQLException e) {
      log.error("Got an error while retrieving airports from db", e);
    }
  }

  public void retrieveCoordinatesFromDB() {
    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      String sql = "SELECT * FROM Coordinates";
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        coordinates.add(
            new Coordinates()
                .setId(resultSet.getInt("id"))
                .setDescription(resultSet.getString("description"))
                .setPosition(resultSet.getDouble("position")));
        log.info("Retrieved coordinates from database");
      }
    } catch (SQLException e) {
      log.error("Got an error while retrieving coordinates from db", e);
    }
  }

  public void insertArrivals(Arrivals ar, String tableName) {

    if (ar.getId() == null) {
      ar.generateId();
    }

    String sql = getArDepInsertSql(ar, tableName);

    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      statement.execute(sql);
      log.info("Added " + tableName + " for airport" + ar.getIcao24());
    } catch (SQLException e) {
      log.error("Got an error while inserting " + tableName, e);
    }
  }

  public String getArDepInsertSql(GenericArDep ar, String tableName) {
    return "INSERT INTO "
        + tableName
        + " (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount)"
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

  @Inject
  public DBSingleton setDs(AgroalDataSource ds) {
    this.ds = ds;
    return this;
  }

  public EntityManager getEm() {
    return em;
  }

  public DBSingleton setEm(EntityManager em) {
    this.em = em;
    return this;
  }

  public Logger getLog() {
    return log;
  }

  public DBSingleton setLog(Logger log) {
    this.log = log;
    return this;
  }

  public List<String> getAirports() {
    return airports;
  }

  public DBSingleton setAirports(List<String> airports) {
    this.airports = airports;
    return this;
  }
  
  public List<Coordinates> getCoordinates() {
    return coordinates;
  }
  
  public DBSingleton setCoordinates(List<Coordinates> coordinates) {
    this.coordinates = coordinates;
    return this;
  }
}
