package org.scaleableandreliable.HTTPclients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.models.Arrivals;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class DepartureClient {
  static final String departureString = "Departures";
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  @Inject DBSingleton instance;
  @Inject Logger log;

  @Scheduled(every = "1h")
  public void sendArrivalRequests() {
    if (instance.getAirports().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }
    instance
        .getAirports()
        .forEach(
            airportId ->
                retrieveDepartureAirportInterval(airportId, getStartTime(), getEndTime())
                    .thenApplyAsync(this::convertAndSave)
                    .thenRunAsync(() -> log.info("Finished inserting departures for " + airportId))
                    .exceptionally(
                        e -> {
                          log.error("Got an exception in the scheduled task", e);
                          return null;
                        }));
  }

  public CompletableFuture<Void> convertAndSave(String json) {
    var g = new Gson();
    for (JsonElement jsonElement : g.fromJson(json, JsonArray.class)) {
      var arr = g.fromJson(jsonElement, Arrivals.class);
      instance.insertArrivals(arr, departureString);
    }
    return new CompletableFuture<>();
  }

  // TODO: Finish me with real times
  String getStartTime() {
    return "1517227200";
  }

  // TODO: Finish me with real times
  public String getEndTime() {
    return "1517230800";
  }

  public CompletionStage<String> retrieveDepartureAirportInterval(
      String airportNumber, String timeStart, String timeEnd) {
    return this.httpClient
        .sendAsync(
            HttpRequest.newBuilder()
                .GET()
                .uri(
                    URI.create(
                        "https://opensky-network.org/api/flights/departure?airport="
                            + airportNumber
                            + "&begin="
                            + timeStart
                            + "&end="
                            + timeEnd))
                .header("Accept", "application/json")
                .build(),
            HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .toCompletableFuture();
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public DepartureClient setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
    return this;
  }

  public DBSingleton getInstance() {
    return instance;
  }

  public DepartureClient setInstance(DBSingleton instance) {
    this.instance = instance;
    return this;
  }

  public Logger getLog() {
    return log;
  }

  public DepartureClient setLog(Logger log) {
    this.log = log;
    return this;
  }
}
