package org.scaleableandreliable.models;

import com.google.gson.JsonArray;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.List;

class CompositeKey implements Serializable {
  private long timeStamp;
  private String icao24;
}

@Entity
@IdClass(CompositeKey.class)
public class AircraftState {
  
  @Id
  private String icao24;
  private String callsign;
  private String originCountry;
  private int timePosition;
  private int lastContact;
  private float longitude;
  private float latitude;
  private float baroAltitude;
  private boolean onGround;
  private float velocity;
  private float trueTrack;
  private float verticalRate;
  @ElementCollection private List<Integer> sensors;
  private float geoAltitude;
  private String squawk;
  private boolean spi;
  private int positionSource;
  private int category;
  @Id private long timeStamp;
  
  public AircraftState() {
  
  }
  public AircraftState(JsonArray arr, long timeStamp) {
    jsonParser(arr, timeStamp);
  }
  
  public String getIcao24() {
    return icao24;
  }

  public AircraftState setIcao24(String icao24) {
    this.icao24 = icao24;
    return this;
  }

  public String getCallsign() {
    return callsign;
  }

  public AircraftState setCallsign(String callsign) {
    this.callsign = callsign;
    return this;
  }

  public String getOriginCountry() {
    return originCountry;
  }

  public AircraftState setOriginCountry(String originCountry) {
    this.originCountry = originCountry;
    return this;
  }

  public int getTimePosition() {
    return timePosition;
  }

  public AircraftState setTimePosition(int timePosition) {
    this.timePosition = timePosition;
    return this;
  }

  public int getLastContact() {
    return lastContact;
  }

  public AircraftState setLastContact(int lastContact) {
    this.lastContact = lastContact;
    return this;
  }

  public float getLongitude() {
    return longitude;
  }

  public AircraftState setLongitude(float longitude) {
    this.longitude = longitude;
    return this;
  }

  public float getLatitude() {
    return latitude;
  }

  public AircraftState setLatitude(float latitude) {
    this.latitude = latitude;
    return this;
  }

  public float getBaroAltitude() {
    return baroAltitude;
  }

  public AircraftState setBaroAltitude(float baroAltitude) {
    this.baroAltitude = baroAltitude;
    return this;
  }

  public boolean isOnGround() {
    return onGround;
  }

  public AircraftState setOnGround(boolean onGround) {
    this.onGround = onGround;
    return this;
  }

  public float getVelocity() {
    return velocity;
  }

  public AircraftState setVelocity(float velocity) {
    this.velocity = velocity;
    return this;
  }

  public float getTrueTrack() {
    return trueTrack;
  }

  public AircraftState setTrueTrack(float trueTrack) {
    this.trueTrack = trueTrack;
    return this;
  }

  public float getVerticalRate() {
    return verticalRate;
  }

  public AircraftState setVerticalRate(float verticalRate) {
    this.verticalRate = verticalRate;
    return this;
  }

  public List<Integer> getSensors() {
    return sensors == null? null : sensors;
  }

  public AircraftState setSensors(List<Integer> sensors) {
    this.sensors = sensors;
    return this;
  }

  public float getGeoAltitude() {
    return geoAltitude;
  }

  public AircraftState setGeoAltitude(float geoAltitude) {
    this.geoAltitude = geoAltitude;
    return this;
  }

  public String getSquawk() {
    return squawk == null? null : squawk;  }

  public AircraftState setSquawk(String squawk) {
    this.squawk = squawk;
    return this;
  }

  public boolean isSpi() {
    return spi;
  }

  public AircraftState setSpi(boolean spi) {
    this.spi = spi;
    return this;
  }

  public int getPositionSource() {
    return positionSource;
  }

  public AircraftState setPositionSource(int positionSource) {
    this.positionSource = positionSource;
    return this;
  }

  public int getCategory() {
    return category;
  }

  public AircraftState setCategory(int category) {
    this.category = category;
    return this;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public AircraftState setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

   AircraftState jsonParser(JsonArray arr, long timestamp) {


    if (!arr.get(0).isJsonNull()) {
      this.setIcao24(arr.get(0).getAsString());
    }

    if (!arr.get(1).isJsonNull()) {
      this.setCallsign(arr.get(1).getAsString());
    }
    if (!arr.get(2).isJsonNull()) {
      this.setOriginCountry(arr.get(2).getAsString());
    }
    if (!arr.get(3).isJsonNull()) {
      this.setTimePosition(arr.get(3).getAsInt());
    }
    if (!arr.get(4).isJsonNull()) {
      this.setLastContact(arr.get(4).getAsInt());
    }
    if (!arr.get(5).isJsonNull()) {
      this.setLongitude(arr.get(5).getAsFloat());
    }
    if (!arr.get(6).isJsonNull()) {
      this.setLatitude(arr.get(6).getAsFloat());
    }
    if (!arr.get(7).isJsonNull()) {
      this.setBaroAltitude(arr.get(7).getAsFloat());
    }
    if (!arr.get(8).isJsonNull()) {
      this.setOnGround(arr.get(8).getAsBoolean());
    }
    if (!arr.get(9).isJsonNull()) {
      this.setVelocity(arr.get(9).getAsFloat());
    }
    if (!arr.get(10).isJsonNull()) {
      this.setTrueTrack(arr.get(10).getAsFloat());
    }
    if (!arr.get(11).isJsonNull()) {
      this.setVerticalRate(arr.get(11).getAsFloat());
    }
    if (!arr.get(12).isJsonNull()) {
      //      this.setSensors(arr.get(12).getas())

    }
    if (!arr.get(13).isJsonNull()) {
      this.setGeoAltitude(arr.get(13).getAsFloat());
    }
    if (!arr.get(14).isJsonNull()) {
      this.setSquawk(arr.get(14).getAsString());
    }
    if (!arr.get(15).isJsonNull()) {
      this.setSpi(arr.get(15).getAsBoolean());
    }
    if (!arr.get(16).isJsonNull()) {
      this.setPositionSource(arr.get(16).getAsInt());
    }

    this.setTimeStamp(timestamp);
    return this;
  }
}
