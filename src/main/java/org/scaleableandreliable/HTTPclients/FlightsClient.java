package org.scaleableandreliable.HTTPclients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.models.AircraftState;
import org.scaleableandreliable.models.Coordinates;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class FlightsClient {

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  @Inject DBSingleton instance;
  @Inject Logger log;

  @Scheduled(every = "1h")
  public void sendArrivalRequests() {
    if (instance.getCoordinates().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }

    retrieveDepartureAirportInterval(instance.getCoordinates(), getStartTime(), getEndTime())
        .thenApplyAsync(this::convertAndSave)
        .thenRunAsync(() -> log.info("Finished inserting all current flights"))
        .exceptionally(
            e -> {
              log.error("Got an exception in the scheduled task", e);
              return null;
            });
  }

  public CompletableFuture<Void> convertAndSave(String json) {
    var g = new Gson();

    var jsonObject = g.fromJson(json, JsonObject.class);

    for (JsonElement jsonElement : jsonObject.get("states").getAsJsonArray()) {
      JsonArray arr = jsonElement.getAsJsonArray();

      instance.insertStates(new AircraftState(arr, jsonObject.get("time").getAsLong()));
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
      List<Coordinates> coordinatesList, String timeStart, String timeEnd) {
    return this.httpClient
        .sendAsync(
            HttpRequest.newBuilder()
                .GET()
                .uri(
                    URI.create(
                        "https://opensky-network.org/api/states/all?lamin="
                            + coordinatesList.stream()
                                .filter(a -> a.getDescription().equalsIgnoreCase("south"))
                                .findFirst()
                                .get()
                                .getPosition()
                            + "&lomin="
                            + coordinatesList.stream()
                                .filter(a -> a.getDescription().equalsIgnoreCase("west"))
                                .findFirst()
                                .get()
                                .getPosition()
                            + "&lamax="
                            + coordinatesList.stream()
                                .filter(a -> a.getDescription().equalsIgnoreCase("north"))
                                .findFirst()
                                .get()
                                .getPosition()
                            + "&lomax="
                            + coordinatesList.stream()
                                .filter(a -> a.getDescription().equalsIgnoreCase("east"))
                                .findFirst()
                                .get()
                                .getPosition()))
                .header("Accept", "application/json")
                .build(),
            HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .toCompletableFuture();
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public FlightsClient setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
    return this;
  }

  public DBSingleton getInstance() {
    return instance;
  }

  public FlightsClient setInstance(DBSingleton instance) {
    this.instance = instance;
    return this;
  }

  public Logger getLog() {
    return log;
  }

  public FlightsClient setLog(Logger log) {
    this.log = log;
    return this;
  }
}
