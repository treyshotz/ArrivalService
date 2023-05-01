package org.scaleableandreliable.HTTPclients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.HTTPclients.ClientHelper.MessageResponse;
import org.scaleableandreliable.models.Arrivals;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.scaleableandreliable.HTTPclients.ClientHelper.handleHTTPResponse;

@ApplicationScoped
public class ArrivalClient {

  static final String arrivalString = "Arrivals";
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  @Inject DBSingleton instance;
  @Inject Logger log;

  void onApplicationStart(@Observes StartupEvent e) {
    collectHistoricalData();
  }

  public void collectHistoricalData() {
    if (instance.getCollects().isEmpty()) {
      instance.retrieveHistoryCollectorFromDB();
    }
    if (instance.getAirports().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }

    var optionalHistoryCollect =
        instance.getCollects().stream()
            .filter(historyCollect -> historyCollect.getType().equalsIgnoreCase("arrival"))
            .findFirst();
    if (optionalHistoryCollect.isPresent()) {
      Instant from = Instant.ofEpochSecond(optionalHistoryCollect.get().getFromDate());
      Instant to = Instant.ofEpochSecond(optionalHistoryCollect.get().getToDate());

      while (from.until(to, ChronoUnit.DAYS) >= 7) {
        var to2 = from.plus(7, ChronoUnit.DAYS);
        sendHistoricalRequest(from, to2);
        from = to2;
      }

      sendHistoricalRequest(from, to);
    }
  }

  private void sendHistoricalRequest(Instant from, Instant to2) {
    instance
        .getAirports()
        .forEach(
            airportId -> {
              var resp =
                  retrieveArrivalsAirportInterval(airportId, String.valueOf(from.getEpochSecond()), String.valueOf(to2.getEpochSecond()));
              convertAndSave(resp);
              log.info("Finished inserting historical arrivals for " + airportId);
            });

    //                    .thenApplyAsync(this::convertAndSave)
    //                    .thenRunAsync(
    //                        () -> log.info("Finished inserting historical arrivals for " +
    // airportId))
    //                    .exceptionally(
    //                        e -> {
    //                          log.error(
    //                              "Got an exception in the historical arrival task for airport; "
    //                                  + airportId,
    //                              e);
    //                          return null;
    //                        }));
  }

  @Scheduled(every = "1m")
  public void sendArrivalRequests() {
    if (instance.getAirports().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }
    instance
        .getAirports()
        .forEach(
            airportId ->
                asyncRetrieveArrivalsAirportInterval(airportId, getStartTime(), getEndTime())
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

  public CompletableFuture<Void> convertAndSave(MessageResponse messageResponse) {
    if (messageResponse.statusCode.charAt(0) == '5'
        || messageResponse.statusCode.charAt(0) == '4') {
      log.error(
          "Got statuscode "
              + messageResponse.statusCode
              + " when retrieving arrivals."
              + messageResponse.message);
      return new CompletableFuture<>();
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

  public MessageResponse retrieveArrivalsAirportInterval(
      String airportNumber, String timeStart, String timeEnd) {
    try {
      var response =
          this.httpClient.send(
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
              HttpResponse.BodyHandlers.ofString());
      return handleHTTPResponse(response);
    } catch (InterruptedException | IOException e) {
      log.error("Got an error while sending arrival request from client", e);
    }
    return null;
  }

  public CompletionStage<MessageResponse> asyncRetrieveArrivalsAirportInterval(
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
        .thenApply(ClientHelper::handleHTTPResponse)
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
