package org.scaleableandreliable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class PostResourceClientTest {

  @InjectMocks PostResourceClient client;

  Logger logMock;

  AirportDBSingleton instance;

  @BeforeEach
  void init_mocks() {
    MockitoAnnotations.openMocks(this);
    logMock = mock(Logger.class);
    client.log = logMock;
    instance = mock(AirportDBSingleton.class);
    client.instance = instance;
  }

  @Test
  void testConvertAndSaveMultiple() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2},{\"icao24\":\"3c6675\",\"firstSeen\":1517227831,\"estDepartureAirport\":\"EDDT\",\"lastSeen\":1517230709,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"DLH187  \",\"estDepartureAirportHorizDistance\":191,\"estDepartureAirportVertDistance\":54,\"estArrivalAirportHorizDistance\":3000,\"estArrivalAirportVertDistance\":103,\"departureAirportCandidatesCount\":1,\"arrivalAirportCandidatesCount\":3}]";
    doNothing().when(instance).insertArrivals(any(Arrivals.class));

    client.convertAndSave(json);

    verify(instance, times(2)).insertArrivals(any(Arrivals.class));
  }

  @Test
  void testConvertAndSaveSingle() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2}]";
    doNothing().when(instance).insertArrivals(any(Arrivals.class));

    client.convertAndSave(json);

    verify(instance, times(1)).insertArrivals(any(Arrivals.class));
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
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.httpClient = httpClient;

    client.retrieveArrivalsAirportInterval("", "", "");

    verify(httpClient, times(1)).sendAsync(any(), any());
  }

  @Test
  void testRetrieveArrivalsAirportIntervalMultiple() {
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.httpClient = httpClient;

    client.retrieveArrivalsAirportInterval("", "", "");
    client.retrieveArrivalsAirportInterval("", "", "");

    verify(httpClient, times(2)).sendAsync(any(), any());
  }

  @Test
  void testSendArrival() {
    instance.airports = spy(new ArrayList<>(List.of("ABC")));

    client.sendArrivalRequests();

    verify(instance.airports, times(1)).forEach(any());
  }

  @Test
  void testSendArrivalException() {
    //    client.instance.airports = List.of("ABCD");
    instance.airports = spy(new ArrayList<>(List.of("ABC")));
    client.httpClient = mock(HttpClient.class);

    doReturn(CompletableFuture.failedFuture(new Throwable()))
        .when(client.httpClient)
        .sendAsync(any(), any());

    client.sendArrivalRequests();

    verify(logMock, times(1)).error(anyString(), any(Throwable.class));
  }
}
