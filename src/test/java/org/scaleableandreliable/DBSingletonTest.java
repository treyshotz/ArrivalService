package org.scaleableandreliable;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.models.AircraftState;
import org.scaleableandreliable.models.Arrivals;

import java.sql.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class DBSingletonTest {

  @InjectMocks DBSingleton instance;

  AgroalDataSource dsMock;
  Logger logMock;

  static final String arrivalString = "Arrivals";
  static final String departureString = "Departures";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    dsMock = mock(AgroalDataSource.class);
    instance.setDs(dsMock);
    logMock = mock(Logger.class);
    instance.setLog(logMock);
  }

  @Test
  void testRetrieveAirportsFromDB() throws SQLException {
    var connMock = mock(Connection.class);
    var stateMock = mock(Statement.class);
    var resultMock = mock(ResultSet.class);

    doReturn(stateMock).when(connMock).createStatement();
    doReturn(connMock).when(dsMock).getConnection();
    doReturn(resultMock).when(stateMock).getResultSet();
    when(resultMock.next()).thenReturn(true, false);
    doReturn("EDDF").when(resultMock).getString(anyString());

    instance.retrieveAirportsFromDB();

    verify(resultMock, times(2)).next();
    assertThat(instance.getAirports().size(), is(1));
  }

  @Test
  void testExceptionOnRetrieveAirportsFromDB() throws SQLException {
    var connMock = mock(Connection.class);
    var stateMock = mock(Statement.class);
    var resultMock = mock(ResultSet.class);

    doReturn(stateMock).when(connMock).createStatement();
    doReturn(connMock).when(this.dsMock).getConnection();
    doReturn(resultMock).when(stateMock).getResultSet();
    doThrow(SQLException.class).when(stateMock).execute(anyString());

    instance.retrieveAirportsFromDB();

    verify(resultMock, times(0)).next();
    verify(logMock, times(1)).error(anyString(), any(SQLException.class));
    assertThat(instance.getAirports().size(), is(0));
  }

  @Test
  void testInsertArrivals() throws SQLException {
    var connMock = mock(Connection.class);
    var stateMock = mock(PreparedStatement.class);
    var arrival =
        new Arrivals()
            .setId(1L)
            .setIcao24("abc")
            .setFirstSeen(0)
            .setEstDepartureAirport("abc")
            .setLastSeen(0)
            .setEstArrivalAirport("abc")
            .setCallsign("abc")
            .setEstDepartureAirportHorizDistance(0)
            .setEstDepartureAirportVertDistance(0)
            .setEstArrivalAirportHorizDistance(0)
            .setEstArrivalAirportVertDistance(0)
            .setDepartureAirportCandidatesCount(0)
            .setArrivalAirportCandidatesCount(0);

    doReturn(stateMock).when(connMock).prepareStatement(anyString());
    doReturn(connMock).when(dsMock).getConnection();

    instance.insertArrDep(arrival, "Arrivals");

    verify(logMock, times(1)).info(anyString());
    verify(stateMock, times(1)).execute();
  }

  @Test
  void testInsertStates() throws SQLException {
    var connMock = mock(Connection.class);
    var stateMock = mock(PreparedStatement.class);
  
    doReturn(stateMock).when(connMock).prepareStatement(anyString());
    doReturn(connMock).when(dsMock).getConnection();

    var testState = new AircraftState(
        "ABC", "ABC", "NOR", 123, 123, 12.3f, 12.3f, 12.3f, false, 321f, 123f, 123f, null, 123.0f,
        "ABC", false, 4, 4, 123L);

    instance.insertStates(List.of(testState));
    verify(logMock, times(1)).info(anyString());
    verify(stateMock, times(1)).executeBatch();
  }

  @Test
  void testGetSql() {
    String expected =
        "INSERT INTO Arrivals (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    assertThat(expected, is(instance.getArDepInsertSql(arrivalString)));
  }
}
