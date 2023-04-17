package org.scaleableandreliable;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.scheduler.Scheduler;

import javax.inject.Inject;
import java.util.List;

@QuarkusMain
public class Main {


  public static void main(String[] args) {
    // Load the airports on startup and store in "cache"
    AirportDBSingelton.getInstance().RetrieveAirportsFromDB();
    Quarkus.run(args);
  
  }
}
