package org.scaleableandreliable.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class Arrivals extends GenericArDep {

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

  @Override
  public String getIcao24() {
    return icao24;
  }

  @Override
  public Arrivals setIcao24(String icao24) {
    this.icao24 = icao24;
    return this;
  }

  @Override
  public int getFirstSeen() {
    return firstSeen;
  }

  @Override
  public Arrivals setFirstSeen(int firstSeen) {
    this.firstSeen = firstSeen;
    return this;
  }

  @Override
  public String getEstDepartureAirport() {
    return estDepartureAirport;
  }

  @Override
  public Arrivals setEstDepartureAirport(String estDepartureAirport) {
    this.estDepartureAirport = estDepartureAirport;
    return this;
  }

  @Override
  public int getLastSeen() {
    return lastSeen;
  }

  @Override
  public Arrivals setLastSeen(int lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  @Override
  public String getEstArrivalAirport() {
    return estArrivalAirport;
  }

  @Override
  public Arrivals setEstArrivalAirport(String estArrivalAirport) {
    this.estArrivalAirport = estArrivalAirport;
    return this;
  }

  @Override
  public String getCallsign() {
    return callsign;
  }

  @Override
  public Arrivals setCallsign(String callsign) {
    this.callsign = callsign;
    return this;
  }

  @Override
  public int getEstDepartureAirportHorizDistance() {
    return estDepartureAirportHorizDistance;
  }

  @Override
  public Arrivals setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
    this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
    return this;
  }

  @Override
  public int getEstDepartureAirportVertDistance() {
    return estDepartureAirportVertDistance;
  }

  @Override
  public Arrivals setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
    this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
    return this;
  }

  @Override
  public int getEstArrivalAirportHorizDistance() {
    return estArrivalAirportHorizDistance;
  }

  @Override
  public Arrivals setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
    this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
    return this;
  }

  @Override
  public int getEstArrivalAirportVertDistance() {
    return estArrivalAirportVertDistance;
  }

  @Override
  public Arrivals setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
    this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
    return this;
  }

  @Override
  public int getDepartureAirportCandidatesCount() {
    return departureAirportCandidatesCount;
  }

  @Override
  public Arrivals setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
    this.departureAirportCandidatesCount = departureAirportCandidatesCount;
    return this;
  }

  @Override
  public int getArrivalAirportCandidatesCount() {
    return arrivalAirportCandidatesCount;
  }

  @Override
  public Arrivals setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
    this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
    return this;
  }

  @Override
  public Long getId() {
    if (this.id == null) {
      generateId();
    }
    return id;
  }

  @Override
  public Arrivals setId(Long id) {
    this.id = id;
    return this;
  }

  public void generateId() {
    this.id = new Random().nextLong();
  }
}
