package org.scaleableandreliable.HTTPclients;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;

public class ClientHelper {

  @Inject Logger log;

  public static MessageResponse handleHTTPResponse(HttpResponse<String> msg) {
    return new MessageResponse()
        .setMessage(msg.body())
        .setStatusCode(String.valueOf(msg.statusCode()));
  }

  public static MessageResponse retrieveArrivalsAirportInterval(
      String airportNumber,
      String timeStart,
      String timeEnd,
      String uriEndpoint,
      HttpClient httpClient)
      throws IOException, InterruptedException {
    var response =
        httpClient.send(
            HttpRequest.newBuilder()
                .GET()
                .uri(
                    URI.create(
                        "https://opensky-network.org/api/flights/"
                            + uriEndpoint
                            + "?airport="
                            + airportNumber
                            + "&begin="
                            + timeStart
                            + "&end="
                            + timeEnd))
                .header("Accept", "application/json")
                .build(),
            HttpResponse.BodyHandlers.ofString());
    return handleHTTPResponse(response);
  }

  public static CompletionStage<MessageResponse> asyncRetrieveDepartureAirportInterval(
      String airportNumber,
      String timeStart,
      String timeEnd,
      String uriEndpoint,
      HttpClient httpClient) {
    return httpClient
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
        .thenApply(ClientHelper::handleHTTPResponse)
        .toCompletableFuture();
  }
  
  // TODO: Finish me with real times
  public static String getStartTime() {
    return String.valueOf(Instant.now().minus(30, ChronoUnit.MINUTES).getEpochSecond());
  }
  
  // TODO: Finish me with real times
  public static String getEndTime() {
    return String.valueOf(Instant.now().getEpochSecond());
  }
  
  
  public static class MessageResponse {
    String message;
    String statusCode;

    public String getMessage() {
      return message;
    }

    public MessageResponse setMessage(String message) {
      this.message = message;
      return this;
    }

    public String getStatusCode() {
      return statusCode;
    }

    public MessageResponse setStatusCode(String statusCode) {
      this.statusCode = statusCode;
      return this;
    }
  }
}
