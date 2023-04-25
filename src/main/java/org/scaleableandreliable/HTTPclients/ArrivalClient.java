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
import javax.ws.rs.BadRequestException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ArrivalClient {
  static final String arrivalString = "Arrivals";
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  @Inject DBSingleton instance;
  @Inject Logger log;

  @Scheduled(every = "1m")
  public void sendArrivalRequests() {
    if (instance.getAirports().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }
    instance
        .getAirports()
        .forEach(
            airportId ->
                retrieveArrivalsAirportInterval(airportId, getStartTime(), getEndTime())
                    .thenApplyAsync(this::convertAndSave)
                    .thenRunAsync(() -> log.info("Finished inserting arrivals for " + airportId))
                    .exceptionally(
                        e -> {
                          log.error(
                              "Got an exception in the scheduled task for airport; " + airportId,
                              e);
                          return null;
                        }));
  }
  
  public CompletableFuture<Void> convertAndSave(DepartureClient.MessageResponse messageResponse) {
    if (messageResponse.statusCode.charAt(0) == '5'
            || messageResponse.statusCode.charAt(0) == '4') {
      throw new BadRequestException(
              "Got statuscode "
                      + messageResponse.statusCode
                      + " when retrieving arrivals."
                      + messageResponse.message);
    }
    var json = messageResponse.message;

    var g = new Gson();
    for (JsonElement jsonElement : g.fromJson(json, JsonArray.class)) {
      var arr = g.fromJson(jsonElement, Arrivals.class);
      instance.insertArrDep(arr, arrivalString);
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
  
  DepartureClient.MessageResponse handleHTTPResponse(HttpResponse<String> msg) {
    return new DepartureClient.MessageResponse()
            .setMessage(msg.body())
            .setStatusCode(String.valueOf(msg.statusCode()));
  }
  
  public CompletionStage<DepartureClient.MessageResponse> retrieveArrivalsAirportInterval(
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
        .thenApply(this::handleHTTPResponse)
        .toCompletableFuture();
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public ArrivalClient setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
    return this;
  }

  public DBSingleton getInstance() {
    return instance;
  }

  public ArrivalClient setInstance(DBSingleton instance) {
    this.instance = instance;
    return this;
  }

  public Logger getLog() {
    return log;
  }

  public ArrivalClient setLog(Logger log) {
    this.log = log;
    return this;
  }
}
