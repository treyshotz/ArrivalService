package org.scaleableandreliable.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class Departures extends GenericArDep {

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
  @Id Long id;

  public String getIcao24() {
    return icao24;
  }

  public Departures setIcao24(String icao24) {
    this.icao24 = icao24;
    return this;
  }

  public int getFirstSeen() {
    return firstSeen;
  }

  public Departures setFirstSeen(int firstSeen) {
    this.firstSeen = firstSeen;
    return this;
  }

  public String getEstDepartureAirport() {
    return estDepartureAirport;
  }

  public Departures setEstDepartureAirport(String estDepartureAirport) {
    this.estDepartureAirport = estDepartureAirport;
    return this;
  }

  public int getLastSeen() {
    return lastSeen;
  }

  public Departures setLastSeen(int lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  public String getEstArrivalAirport() {
    return estArrivalAirport;
  }

  public Departures setEstArrivalAirport(String estArrivalAirport) {
    this.estArrivalAirport = estArrivalAirport;
    return this;
  }

  public String getCallsign() {
    return callsign;
  }

  public Departures setCallsign(String callsign) {
    this.callsign = callsign;
    return this;
  }

  public int getEstDepartureAirportHorizDistance() {
    return estDepartureAirportHorizDistance;
  }

  public Departures setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
    this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
    return this;
  }

  public int getEstDepartureAirportVertDistance() {
    return estDepartureAirportVertDistance;
  }

  public Departures setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
    this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
    return this;
  }

  public int getEstArrivalAirportHorizDistance() {
    return estArrivalAirportHorizDistance;
  }

  public Departures setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
    this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
    return this;
  }

  public int getEstArrivalAirportVertDistance() {
    return estArrivalAirportVertDistance;
  }

  public Departures setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
    this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
    return this;
  }

  public int getDepartureAirportCandidatesCount() {
    return departureAirportCandidatesCount;
  }

  public Departures setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
    this.departureAirportCandidatesCount = departureAirportCandidatesCount;
    return this;
  }

  public int getArrivalAirportCandidatesCount() {
    return arrivalAirportCandidatesCount;
  }

  public Departures setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
    this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
    return this;
  }

  public Long getId() {
    if (this.id == null) {
      generateId();
    }
    return id;
  }

  public Departures setId(Long id) {
    this.id = id;
    return this;
  }

  public void generateId() {
    this.id = new Random().nextLong();
  }
}
