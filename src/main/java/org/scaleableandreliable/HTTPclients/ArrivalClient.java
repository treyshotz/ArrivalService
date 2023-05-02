package org.scaleableandreliable.HTTPclients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.models.Arrivals;
import org.scaleableandreliable.models.HistoryCollect;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.scaleableandreliable.HTTPclients.ClientHelper.*;

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
            .filter(historyCollect -> historyCollect.getType().equalsIgnoreCase(arrivalString))
            .findFirst();
    optionalHistoryCollect.ifPresent(this::iterateHistoricalCollect);
  }

  private void iterateHistoricalCollect(HistoryCollect historyCollect) {
    Instant from = Instant.ofEpochSecond(historyCollect.getFromDate());
    Instant to = Instant.ofEpochSecond(historyCollect.getToDate());

    while (from.until(to, ChronoUnit.DAYS) >= 7) {
      var to2 = from.plus(7, ChronoUnit.DAYS);
      sendHistoricalRequest(from, to2);
      from = to2;
    }

    sendHistoricalRequest(from, to);
    instance.setHistoryCollectToFalse(historyCollect);
  }

  private void sendHistoricalRequest(Instant from, Instant to2) {
    instance
        .getAirports()
        .forEach(
            airportId -> {
              var ref =
                  new Object() {
                    MessageResponse resp = null;
                  };
              try {
                ref.resp =
                    retrieveArrivalsAirportInterval(
                        airportId,
                        String.valueOf(from.getEpochSecond()),
                        String.valueOf(to2.getEpochSecond()),
                        "arrival",
                        this.httpClient);
              } catch (InterruptedException | IOException e) {
                log.error("Got an error while sending arrival request from client", e);
              }
              this.executorService.execute(() -> convertAndSave(ref.resp));
              log.info("Finished inserting historical arrivals for " + airportId);
            });
  }

  @Scheduled(every = "30m")
  public void sendArrivalRequests() {
    if (instance.getAirports().isEmpty()) {
      instance.retrieveAirportsFromDB();
    }

    instance
        .getAirports()
        .forEach(
            airportId -> {
              var ref =
                  new Object() {
                    MessageResponse resp = null;
                  };
              try {
                ref.resp =
                    retrieveArrivalsAirportInterval(
                        airportId, getStartTime(), getEndTime(), "arrival", this.httpClient);
              } catch (InterruptedException | IOException e) {
                log.error("Got an exception in the scheduled task for airport; " + airportId, e);
              }
              this.executorService.execute(() -> convertAndSave(ref.resp));
              log.info("Finished inserting scheduled arrivals for " + airportId);
            });
  }

  public CompletableFuture<Void> convertAndSave(MessageResponse messageResponse) {
    CompletableFuture<Void> x = checkError(messageResponse);
    if (x != null) return x;
    var json = messageResponse.message;

    var g = new Gson();
    var list = new ArrayList<Arrivals>();
    for (JsonElement jsonElement : g.fromJson(json, JsonArray.class)) {
      list.add(g.fromJson(jsonElement, Arrivals.class));
    }
    instance.insertBatchArrDep(list, arrivalString);
    return new CompletableFuture<>();
  }

  private CompletableFuture<Void> checkError(MessageResponse messageResponse) {
    if (messageResponse.statusCode.charAt(0) == '5'
        || messageResponse.statusCode.charAt(0) == '4') {
      log.error(
          "Got statuscode "
              + messageResponse.statusCode
              + " when retrieving arrivals. "
              + messageResponse.message);
      return new CompletableFuture<>();
    }
    return null;
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
