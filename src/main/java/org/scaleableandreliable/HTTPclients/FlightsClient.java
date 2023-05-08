package org.scaleableandreliable.HTTPclients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;
import org.scaleableandreliable.DBhandlers.DBSingleton;
import org.scaleableandreliable.HTTPclients.ClientHelper.MessageResponse;
import org.scaleableandreliable.models.AircraftState;
import org.scaleableandreliable.models.Coordinates;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class FlightsClient {

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  @Inject DBSingleton instance;
  @Inject Logger log;

  @Scheduled(every = "20m")
  public void sendArrivalRequests() {
    if (instance.getCoordinates().isEmpty()) {
      instance.retrieveCoordinatesFromDB();
    }

    try {
      var resp = retrieveAllStates(instance.getCoordinates());
      convertAndSave(resp);

      log.info("Finished inserting all current flights");

    } catch (InterruptedException | IOException e) {
      log.error("Got an exception in the scheduled task when retrieving states; ", e);
    }
  }

  public CompletableFuture<Void> convertAndSave(MessageResponse messageResponse) {
    CompletableFuture<Void> x = checkError(messageResponse);
    if (x != null) return x;
    var json = messageResponse.message;
    var g = new Gson();

    var jsonObject = g.fromJson(json, JsonObject.class);
    var list = new ArrayList<AircraftState>();
    for (JsonElement jsonElement : jsonObject.get("states").getAsJsonArray()) {
      JsonArray arr = jsonElement.getAsJsonArray();
      list.add(new AircraftState(arr, jsonObject.get("time").getAsLong()));
    }
    instance.insertStates(list);
    return new CompletableFuture<>();
  }

  private CompletableFuture<Void> checkError(MessageResponse messageResponse) {
    if (messageResponse.statusCode.charAt(0) == '5'
        || messageResponse.statusCode.charAt(0) == '4') {
      log.error(
          "Got statuscode "
              + messageResponse.statusCode
              + " when retrieving arrivals."
              + messageResponse.message);
      return new CompletableFuture<>();
    }
    return null;
  }

  MessageResponse handleHTTPResponse(HttpResponse<String> msg) {
    return new MessageResponse()
        .setMessage(msg.body())
        .setStatusCode(String.valueOf(msg.statusCode()));
  }

  public MessageResponse retrieveAllStates(List<Coordinates> coordinatesList)
      throws IOException, InterruptedException {
    var resp =
        this.httpClient.send(
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
            HttpResponse.BodyHandlers.ofString());
    return handleHTTPResponse(resp);
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
