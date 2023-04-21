package org.scaleableandreliable;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class AirportDBSingletonTest {

  @InjectMocks AirportDBSingleton instance;

  AgroalDataSource dsMock;
  Logger logMock;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    dsMock = mock(AgroalDataSource.class);
    instance.setDs(dsMock);
    logMock = mock(Logger.class);
    instance.log = logMock;
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
    assertThat(instance.airports.size(), is(1));
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
    assertThat(instance.airports.size(), is(0));
  }

  @Test
  void testInsertArrivals() throws SQLException {
    var connMock = mock(Connection.class);
    var stateMock = mock(Statement.class);
    var arrival =
        new Arrivals()
            .setId(1L)
            .setIcao24("")
            .setFirstSeen(0)
            .setEstDepartureAirport("")
            .setLastSeen(0)
            .setEstArrivalAirport("")
            .setCallsign("")
            .setEstDepartureAirportHorizDistance(0)
            .setEstDepartureAirportVertDistance(0)
            .setEstArrivalAirportHorizDistance(0)
            .setEstArrivalAirportVertDistance(0)
            .setDepartureAirportCandidatesCount(0)
            .setArrivalAirportCandidatesCount(0);

    doReturn(stateMock).when(connMock).createStatement();
    doReturn(connMock).when(dsMock).getConnection();

    instance.insertArrivals(arrival);

    verify(logMock, times(1)).info(anyString());
    verify(stateMock, times(1)).execute(anyString());
  }

  @Test
  void testGetSql() {
    var arrival =
        new Arrivals()
            .setIcao24("0101be")
            .setFirstSeen(1517220729)
            .setEstDepartureAirport(null)
            .setLastSeen(1517230737)
            .setEstArrivalAirport("EDDF")
            .setCallsign("MSR785  ")
            .setEstDepartureAirportHorizDistance(0)
            .setEstDepartureAirportVertDistance(0)
            .setEstArrivalAirportHorizDistance(1593)
            .setDepartureAirportCandidatesCount(0)
            .setEstArrivalAirportVertDistance(95)
            .setArrivalAirportCandidatesCount(2)
            .setId(123L);

    String expected =
        "INSERT INTO Arrivals (id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount) VALUES (123,'0101be',1517220729,'null',1517230737,'EDDF','MSR785  ',0,0,1593,95,0,2);";
    assertThat(expected, is(instance.getSql(arrival)));
  }
}
