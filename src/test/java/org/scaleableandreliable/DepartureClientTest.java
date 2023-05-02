package org.scaleableandreliable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.HTTPclients.ClientHelper.MessageResponse;
import org.scaleableandreliable.HTTPclients.DepartureClient;
import org.scaleableandreliable.models.Arrivals;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class DepartureClientTest {

  @InjectMocks DepartureClient client;

  Logger logMock;

  DBSingleton instance;

  @BeforeEach
  void init_mocks() {
    MockitoAnnotations.openMocks(this);
    logMock = mock(Logger.class);
    client.setLog(logMock);
    instance = mock(DBSingleton.class);
    client.setInstance(instance);
  }

  @Test
  void testConvertAndSaveMultiple() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2},{\"icao24\":\"3c6675\",\"firstSeen\":1517227831,\"estDepartureAirport\":\"EDDT\",\"lastSeen\":1517230709,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"DLH187  \",\"estDepartureAirportHorizDistance\":191,\"estDepartureAirportVertDistance\":54,\"estArrivalAirportHorizDistance\":3000,\"estArrivalAirportVertDistance\":103,\"departureAirportCandidatesCount\":1,\"arrivalAirportCandidatesCount\":3}]";
    String statusCode = "200";

    doNothing().when(instance).insertArrDep(any(Arrivals.class), anyString());

    client.convertAndSave(
        new MessageResponse().setMessage(json).setStatusCode(statusCode));

    verify(instance, times(2)).insertArrDep(any(Arrivals.class), anyString());
  }

  @Test
  void testConvertAndSaveSingle() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2}]";

    String statusCode = "200";

    doNothing().when(instance).insertArrDep(any(Arrivals.class), anyString());

    client.convertAndSave(
        new MessageResponse().setMessage(json).setStatusCode(statusCode));

    verify(instance, times(1)).insertArrDep(any(Arrivals.class), anyString());
  }

  @Test
  void testGetStartTime() {
    assertThat(client.getEndTime(), isA(String.class));
  }

  @Test
  void testGetEndTime() {
    assertThat(client.getEndTime(), isA(String.class));
  }

  @Test
  void testRetrieveArrivalsAirportIntervalSingle() {
    var httpMock = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpMock)
        .sendAsync(any(), any());
    client.setHttpClient(httpMock);

//    client.retrieveDepartureAirportInterval("", "", "");

    verify(httpMock, times(1)).sendAsync(any(), any());
  }

  @Test
  @Disabled("Use this for helper class")
  void testRetrieveArrivalsAirportIntervalMultiple() {
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.setHttpClient(httpClient);

//    client.retrieveDepartureAirportInterval("", "", "");
//    client.retrieveDepartureAirportInterval("", "", "");

    verify(httpClient, times(2)).sendAsync(any(), any());
  }

  @Test
  @Disabled("Seems to call real method but does not register it")
  void testSendArrival() {
    var abc = spy(new ArrayList<String>(List.of("ABC")));
    when(client.getInstance().setAirports(any())).thenCallRealMethod();
    doCallRealMethod().when(abc).forEach(any());
    when(abc.isEmpty()).thenReturn(false);

    client.getInstance().setAirports(abc);
    client.sendArrivalRequests();

    verify(abc, times(1)).forEach(any());
  }

  @Test
  @Disabled("Seems to call real method but does not register it")
  void testSendArrivalException() {
    //    client.instance.airports = List.of("ABCD");
    instance.setAirports(spy(new ArrayList<>(List.of("ABC"))));
    client.setHttpClient(mock(HttpClient.class));

    doReturn(CompletableFuture.failedFuture(new Throwable()))
        .when(client.getHttpClient())
        .sendAsync(any(), any());

    client.sendArrivalRequests();

    verify(logMock, times(1)).error(anyString(), any(Throwable.class));
  }
}
