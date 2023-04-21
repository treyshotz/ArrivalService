package org.scaleableandreliable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class PostResourceClientTest {

  @InjectMocks PostResourceClient client;

  AutoCloseable closeable;

  @BeforeEach
  void init_mocks() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @Test
  void testConvertAndSaveMultiple() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2},{\"icao24\":\"3c6675\",\"firstSeen\":1517227831,\"estDepartureAirport\":\"EDDT\",\"lastSeen\":1517230709,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"DLH187  \",\"estDepartureAirportHorizDistance\":191,\"estDepartureAirportVertDistance\":54,\"estArrivalAirportHorizDistance\":3000,\"estArrivalAirportVertDistance\":103,\"departureAirportCandidatesCount\":1,\"arrivalAirportCandidatesCount\":3}]";
    var instance = mock(AirportDBSingleton.class);
    doNothing().when(instance).insertArrivals(any(Arrivals.class));
    client.instance = instance;

    client.convertAndSave(json);

    verify(instance, times(2)).insertArrivals(any(Arrivals.class));
  }

  @Test
  void testConvertAndSaveSingle() {
    String json =
        " [{\"icao24\":\"0101be\",\"firstSeen\":1517220729,\"estDepartureAirport\":null,\"lastSeen\":1517230737,\"estArrivalAirport\":\"EDDF\",\"callsign\":\"MSR785  \",\"estDepartureAirportHorizDistance\":null,\"estDepartureAirportVertDistance\":null,\"estArrivalAirportHorizDistance\":1593,\"estArrivalAirportVertDistance\":95,\"departureAirportCandidatesCount\":0,\"arrivalAirportCandidatesCount\":2}]";
    var instance = mock(AirportDBSingleton.class);
    doNothing().when(instance).insertArrivals(any(Arrivals.class));
    client.instance = instance;

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
  void testRetrieveArrivalsAirportIntervalSingle() throws ExecutionException, InterruptedException {
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.httpClient = httpClient;

    client.retrieveArrivalsAirportInterval("", "", "");

    verify(httpClient, times(1)).sendAsync(any(), any());
  }

  @Test
  void testRetrieveArrivalsAirportIntervalMultiple()
      throws ExecutionException, InterruptedException {
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.httpClient = httpClient;

    client.retrieveArrivalsAirportInterval("", "", "");
    client.retrieveArrivalsAirportInterval("", "", "");

    verify(httpClient, times(2)).sendAsync(any(), any());
  }
}
