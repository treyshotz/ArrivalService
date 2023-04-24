package org.scaleableandreliable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.HTTPclients.FlightsClient;
import org.scaleableandreliable.models.AircraftState;
import org.scaleableandreliable.models.Coordinates;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(MockDBProfile.class)
class FlightsClientTest {

  @InjectMocks FlightsClient client;

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
        "{\"time\":1682346982,\"states\":[[\"4b1817\",\"SWR200B \",\"Switzerland\",1682346981,1682346981,10.68,42.8389,11582.4,false,209.61,338.85,0,null,11635.74,\"1000\",false,0],[\"4b1812\",\"SWR8CA  \",\"Switzerland\",1682346981,1682346982,9.9064,45.0634,11597.64,false,215.2,327.3,0,null,11574.78,\"1000\",false,0]]}";

    doNothing().when(instance).insertStates(any(AircraftState.class));

    client.convertAndSave(json);

    verify(instance, times(2)).insertStates(any(AircraftState.class));
  }

  @Test
  void testConvertAndSaveSingle() {

    String json =
        "{\"time\":1682346982,\"states\":[[\"4b1817\",\"SWR200B \",\"Switzerland\",1682346981,1682346981,10.68,42.8389,11582.4,false,209.61,338.85,0,null,11635.74,\"1000\",false,0]]}";

    doNothing().when(instance).insertStates(any(AircraftState.class));

    client.convertAndSave(json);

    verify(instance, times(1)).insertStates(any(AircraftState.class));
  }

  @Test
  void testRetrieveArrivalsAirportIntervalSingle() {
    var httpMock = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpMock)
        .sendAsync(any(), any());
    client.setHttpClient(httpMock);
  
    client.retrieveAllStates(
            List.of(
                    new Coordinates().setDescription("South"),
                    new Coordinates().setDescription("North"),
                    new Coordinates().setDescription("East"),
                    new Coordinates().setDescription("West")));
    verify(httpMock, times(1)).sendAsync(any(), any());
  }

  @Test
  void testRetrieveArrivalsAirportIntervalMultiple() {
    var httpClient = mock(HttpClient.class);
    doReturn(new CompletableFuture<>().minimalCompletionStage())
        .when(httpClient)
        .sendAsync(any(), any());
    client.setHttpClient(httpClient);

    client.retrieveAllStates(
        List.of(
            new Coordinates().setDescription("South"),
            new Coordinates().setDescription("North"),
            new Coordinates().setDescription("East"),
            new Coordinates().setDescription("West")));
    client.retrieveAllStates(
        List.of(
            new Coordinates().setDescription("South"),
            new Coordinates().setDescription("North"),
            new Coordinates().setDescription("East"),
            new Coordinates().setDescription("West")));
    verify(httpClient, times(2)).sendAsync(any(), any());
  }
}
