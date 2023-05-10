package org.scaleableandreliable;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.scaleableandreliable.HTTPclients.ArrivalClient;
import org.scaleableandreliable.HTTPclients.DepartureClient;

import javax.inject.Inject;

@QuarkusMain
public class Main {
  public static void main(String... args) {
    Quarkus.run(MyApp.class, args);
  }

  public static class MyApp implements QuarkusApplication {

    @Inject DepartureClient departureClient;
    @Inject ArrivalClient arrivalClient;

    @Override
    public int run(String... args) throws Exception {
      departureClient.collectHistoricalData();
      arrivalClient.collectHistoricalData();
      Quarkus.waitForExit();
      return 0;
    }
  }
}
