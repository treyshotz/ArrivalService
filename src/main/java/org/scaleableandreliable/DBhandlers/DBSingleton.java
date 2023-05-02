package org.scaleableandreliable.DBhandlers;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import org.scaleableandreliable.models.AircraftState;
import org.scaleableandreliable.models.Coordinates;
import org.scaleableandreliable.models.GenericArDep;
import org.scaleableandreliable.models.HistoryCollect;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DBSingleton {

  static DBSingleton instance;
  static String sqlArrDep = "";
  static String sqlState =
      "INSERT INTO AircraftState (icao24, callsign, originCountry, timePosition, lastContact,"
          + "longitude, latitude, baroAltitude, onGround, velocity, trueTrack, verticalRate,"
          + "sensors, geoAltitude, squawk, spi, positionSource, category, timeStamp)"
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  @Inject Logger log;
  // Will use this as a cache
  List<String> airports;
  List<Coordinates> coordinates;
  List<HistoryCollect> collects;
  private AgroalDataSource ds;
  private EntityManager em;

  public DBSingleton() {
    this.airports = new ArrayList<>();
    this.coordinates = new ArrayList<>();
    this.collects = new ArrayList<>();
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
    retrieveHistoryCollectorFromDB();
  }

  public void retrieveHistoryCollectorFromDB() {
    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      String sql =
          "SELECT h.type, h.from_date, h.to_date, h.active FROM HistoryCollect h WHERE h.active = 1";
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        collects.add(
            new HistoryCollect(
                resultSet.getString("type"),
                resultSet.getLong("from_date"),
                resultSet.getLong("to_date"),
                resultSet.getBoolean("active")));
        log.info("Retrieved history collect  from database");
      }
    } catch (SQLException e) {
      log.error("Got an error while retrieving history collects from db", e);
    }
  }

  public void setHistoryCollectToFalse(HistoryCollect hist) {
    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      String sql = "UPDATE HistoryCollect SET active = 0 WHERE id = " + hist.getId();
      statement.execute(sql);
      log.info("Set history collect for " + hist.getType() + " to false");

    } catch (SQLException e) {
      log.error("Got an error while setting historycollect for " + hist.getType() + " to false", e);
    }
  }

  public void retrieveAirportsFromDB() {

    try (var session = ds.getConnection();
        var statement = session.createStatement()) {
      String sql = "SELECT a.icao24 FROM Airports a WHERE a.active = 1";
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

  public void insertStates(AircraftState aircraftState) {
    try (var session = ds.getConnection();
        var statement = session.prepareStatement(sqlState)) {

      prepareInsert(statement, aircraftState);
      statement.execute();

      log.info("Added state for icao" + aircraftState.getIcao24());
    } catch (SQLException e) {
      log.error("Got an error while inserting " + aircraftState.getIcao24(), e);
    }
  }

  public void insertBatchArrDep(List<? extends GenericArDep> ar, String tableName) {

    try (var session = ds.getConnection();
        var statement = session.prepareStatement(getArDepInsertSql(tableName))) {

      ar.forEach(
          genericArDep -> {
            if (genericArDep.getId() == null) {
              genericArDep.generateId();
            }
            try {
              prepareInsert(statement, genericArDep);
              statement.addBatch();
            } catch (SQLException e) {
              log.error(e);
            }
          });

      statement.executeBatch();

      log.info("Added " + ar.size() + " " + tableName + " for airport" + ar.get(0).getIcao24());
    } catch (SQLException e) {
      log.error("Got an error while inserting " + tableName, e);
    }
  }

  public void insertArrDep(GenericArDep ar, String tableName) {

    if (ar.getId() == null) {
      ar.generateId();
    }

    try (var session = ds.getConnection();
        var statement = session.prepareStatement(getArDepInsertSql(tableName))) {

      prepareInsert(statement, ar);
      statement.execute();

      log.info("Added " + tableName + " for airport" + ar.getIcao24());
    } catch (SQLException e) {
      log.error("Got an error while inserting " + tableName, e);
    }
  }

  public String getArDepInsertSql(String tableName) {
    return "INSERT INTO "
        + tableName
        + " (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  }

  public void prepareInsert(PreparedStatement statement, GenericArDep ar) throws SQLException {
    statement.setLong(1, ar.getId());
    statement.setString(2, ar.getIcao24());
    statement.setInt(3, ar.getFirstSeen());
    statement.setString(4, ar.getEstDepartureAirport());
    statement.setInt(5, ar.getLastSeen());
    statement.setString(6, ar.getEstArrivalAirport());
    statement.setString(7, ar.getCallsign());
    statement.setInt(8, ar.getEstDepartureAirportHorizDistance());
    statement.setInt(9, ar.getEstDepartureAirportVertDistance());
    statement.setInt(10, ar.getEstArrivalAirportHorizDistance());
    statement.setInt(11, ar.getEstArrivalAirportVertDistance());
    statement.setInt(12, ar.getDepartureAirportCandidatesCount());
    statement.setInt(13, ar.getArrivalAirportCandidatesCount());
  }

  void prepareInsert(PreparedStatement statement, AircraftState aircraftState) throws SQLException {
    statement.setString(1, aircraftState.getIcao24());
    statement.setString(2, aircraftState.getCallsign());
    statement.setString(3, aircraftState.getOriginCountry());
    statement.setInt(4, aircraftState.getTimePosition());
    statement.setInt(5, aircraftState.getLastContact());
    statement.setFloat(6, aircraftState.getLongitude());
    statement.setFloat(7, aircraftState.getLatitude());
    statement.setFloat(8, aircraftState.getBaroAltitude());
    statement.setBoolean(9, aircraftState.isOnGround());
    statement.setFloat(10, aircraftState.getVelocity());
    statement.setFloat(11, aircraftState.getTrueTrack());
    statement.setFloat(12, aircraftState.getVerticalRate());
    // TODO: Fixme
    statement.setString(13, null);
    statement.setFloat(14, aircraftState.getGeoAltitude());
    statement.setString(15, aircraftState.getSquawk());
    statement.setBoolean(16, aircraftState.isSpi());
    statement.setInt(17, aircraftState.getPositionSource());
    statement.setInt(18, aircraftState.getCategory());
    statement.setLong(19, aircraftState.getTimeStamp());
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

  public List<HistoryCollect> getCollects() {
    return collects;
  }

  public DBSingleton setCollects(List<HistoryCollect> collects) {
    this.collects = collects;
    return this;
  }
}
