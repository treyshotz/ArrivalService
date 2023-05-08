package org.scaleableandreliable.models;

import java.util.Random;

public abstract class GenericArDep {

  private transient String icao24;
  private transient int firstSeen;
  private transient String estDepartureAirport;
  private transient int lastSeen;
  private transient String estArrivalAirport;
  private transient String callsign;
  private transient int estDepartureAirportHorizDistance;
  private transient int estDepartureAirportVertDistance;
  private transient int estArrivalAirportHorizDistance;
  private transient int estArrivalAirportVertDistance;
  private transient int departureAirportCandidatesCount;
  private transient int arrivalAirportCandidatesCount;
  private transient Long id;

  public String getIcao24() {
    return icao24;
  }

  public GenericArDep setIcao24(String icao24) {
    this.icao24 = icao24;
    return this;
  }

  public int getFirstSeen() {
    return firstSeen;
  }

  public GenericArDep setFirstSeen(int firstSeen) {
    this.firstSeen = firstSeen;
    return this;
  }

  public String getEstDepartureAirport() {
    return estDepartureAirport;
  }

  public GenericArDep setEstDepartureAirport(String estDepartureAirport) {
    this.estDepartureAirport = estDepartureAirport;
    return this;
  }

  public int getLastSeen() {
    return lastSeen;
  }

  public GenericArDep setLastSeen(int lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  public String getEstArrivalAirport() {
    return estArrivalAirport;
  }

  public GenericArDep setEstArrivalAirport(String estArrivalAirport) {
    this.estArrivalAirport = estArrivalAirport;
    return this;
  }

  public String getCallsign() {
    return callsign;
  }

  public GenericArDep setCallsign(String callsign) {
    this.callsign = callsign;
    return this;
  }

  public int getEstDepartureAirportHorizDistance() {
    return estDepartureAirportHorizDistance;
  }

  public GenericArDep setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
    this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
    return this;
  }

  public int getEstDepartureAirportVertDistance() {
    return estDepartureAirportVertDistance;
  }

  public GenericArDep setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
    this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
    return this;
  }

  public int getEstArrivalAirportHorizDistance() {
    return estArrivalAirportHorizDistance;
  }

  public GenericArDep setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
    this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
    return this;
  }

  public int getEstArrivalAirportVertDistance() {
    return estArrivalAirportVertDistance;
  }

  public GenericArDep setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
    this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
    return this;
  }

  public int getDepartureAirportCandidatesCount() {
    return departureAirportCandidatesCount;
  }

  public GenericArDep setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
    this.departureAirportCandidatesCount = departureAirportCandidatesCount;
    return this;
  }

  public int getArrivalAirportCandidatesCount() {
    return arrivalAirportCandidatesCount;
  }

  public GenericArDep setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
    this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
    return this;
  }

  public Long getId() {
    return id;
  }

  public GenericArDep setId(Long id) {
    this.id = id;
    return this;
  }

  public void generateId() {
    this.id = new Random().nextLong();
  }
}
