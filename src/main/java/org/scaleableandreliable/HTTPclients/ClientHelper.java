package org.scaleableandreliable.HTTPclients;

import java.net.http.HttpResponse;

public class ClientHelper {

  public static MessageResponse handleHTTPResponse(HttpResponse<String> msg) {
    return new MessageResponse()
        .setMessage(msg.body())
        .setStatusCode(String.valueOf(msg.statusCode()));
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
