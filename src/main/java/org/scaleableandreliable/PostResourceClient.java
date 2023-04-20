package org.scaleableandreliable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

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
class PostResourceClient {
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();

  @Inject AirportDBSingleton instance;

  @Inject Logger log;

  @Scheduled(every = "1h")
  void sendArrivalRequests() {
    if (instance.airports.isEmpty()) {
      instance.retrieveAirportsFromDB();
    }
    instance.airports.forEach(
        airportId ->
            retrieveArrivalsAirportInterval(airportId, getStartTime(), getEndTime())
                .thenApplyAsync(this::convertAndSave)
                .thenRunAsync(() -> log.info("Finished inserting for " + airportId)));
  }

  CompletableFuture<Void> convertAndSave(String json) {
    var g = new Gson();
    for (JsonElement jsonElement : g.fromJson(json, JsonArray.class)) {
      var arr = g.fromJson(jsonElement, Arrivals.class);
      instance.insertArrivals(arr);
    }
    return new CompletableFuture<>();
  }

  // TODO: Finish me with real times
  String getStartTime() {
    return "1517227200";
  }

  // TODO: Finish me with real times
  String getEndTime() {
    return "1517230800";
  }

  CompletionStage<String> retrieveArrivalsAirportInterval(
      String airportNumber, String timeStart, String timeEnd) {
    return this.httpClient
        .sendAsync(
            HttpRequest.newBuilder()
                .GET()
                .uri(
                    URI.create(
                        "https://opensky-network.org/api/flights/arrival?airport="
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
}
