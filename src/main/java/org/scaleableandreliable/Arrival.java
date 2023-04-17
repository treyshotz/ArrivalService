package org.scaleableandreliable;

public class Arrival {
  
  String icao24;
  int firstSeen;
  String estDepartureAirport;
  int lastSeen;
  String estArrivalAirport;
  String callsign;
  int estDepartureAirportHorizDistance;
  int estDepartureAirportVertDistance;
  int estArrivalAirportHorizDistance;
  int estArrivalAirportVertDistance;
  int departureAirportCandidatesCount;
  int arrivalAirportCandidatesCount;
  
  public String getIcao24() {
    return icao24;
  }
  
  public Arrival setIcao24(String icao24) {
    this.icao24 = icao24;
    return this;
  }
  
  public int getFirstSeen() {
    return firstSeen;
  }
  
  public Arrival setFirstSeen(int firstSeen) {
    this.firstSeen = firstSeen;
    return this;
  }
  
  public String getEstDepartureAirport() {
    return estDepartureAirport;
  }
  
  public Arrival setEstDepartureAirport(String estDepartureAirport) {
    this.estDepartureAirport = estDepartureAirport;
    return this;
  }
  
  public int getLastSeen() {
    return lastSeen;
  }
  
  public Arrival setLastSeen(int lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }
  
  public String getEstArrivalAirport() {
    return estArrivalAirport;
  }
  
  public Arrival setEstArrivalAirport(String estArrivalAirport) {
    this.estArrivalAirport = estArrivalAirport;
    return this;
  }
  
  public String getCallsign() {
    return callsign;
  }
  
  public Arrival setCallsign(String callsign) {
    this.callsign = callsign;
    return this;
  }
  
  public int getEstDepartureAirportHorizDistance() {
    return estDepartureAirportHorizDistance;
  }
  
  public Arrival setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
    this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
    return this;
  }
  
  public int getEstDepartureAirportVertDistance() {
    return estDepartureAirportVertDistance;
  }
  
  public Arrival setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
    this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
    return this;
  }
  
  public int getEstArrivalAirportHorizDistance() {
    return estArrivalAirportHorizDistance;
  }
  
  public Arrival setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
    this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
    return this;
  }
  
  public int getEstArrivalAirportVertDistance() {
    return estArrivalAirportVertDistance;
  }
  
  public Arrival setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
    this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
    return this;
  }
  
  public int getDepartureAirportCandidatesCount() {
    return departureAirportCandidatesCount;
  }
  
  public Arrival setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
    this.departureAirportCandidatesCount = departureAirportCandidatesCount;
    return this;
  }
  
  public int getArrivalAirportCandidatesCount() {
    return arrivalAirportCandidatesCount;
  }
  
  public Arrival setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
    this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
    return this;
  }
}
