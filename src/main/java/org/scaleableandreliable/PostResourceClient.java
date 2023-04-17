package org.scaleableandreliable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.quarkus.scheduler.Scheduled;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PostResourceClient {
  private final ExecutorService executorService = Executors.newFixedThreadPool(5);
  private final HttpClient httpClient =
      HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();
  
  AirportDBSingelton instance = AirportDBSingelton.getInstance();
  
  @Scheduled(every = "10s")
  void sendArrivalRequests() {
//    AirportDBSingelton.getInstance().airports.forEach(
//            s -> retrieveArrivalsAirportInterval(s, getStartTime(), getEndTime())
//    );

    var eddf = retrieveArrivalsAirportInterval("EDDF", getStartTime(), getEndTime());
  
    String b  = (String) ((CompletableFuture) eddf).join();
  
    
    Gson g = new Gson();
    JsonArray jsonArr= g.fromJson(b, JsonArray.class);
    for (JsonElement jsonElement : jsonArr) {
      Arrival arr = g.fromJson(jsonElement, Arrival.class);
      instance.insertArrivals(arr);
    }

  }
  
  //TODO: Finish me with real times
  String getStartTime() {
    return "1517227200";
  }
  
  //TODO: Finish me with real times
  String getEndTime() {
    return "1517230800";
  }
  
  
  CompletionStage<String> retrieveArrivalsAirportInterval(String airportNumber, String timeStart, String timeEnd) {
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
