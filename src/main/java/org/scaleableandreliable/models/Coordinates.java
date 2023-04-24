package org.scaleableandreliable.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Coordinates {
  @Id private int id;
  private String description;
  private double position;

  public int getId() {
    return id;
  }

  public Coordinates setId(int id) {
    this.id = id;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Coordinates setDescription(String description) {
    this.description = description;
    return this;
  }

  public double getPosition() {
    return position;
  }

  public Coordinates setPosition(double position) {
    this.position = position;
    return this;
  }
}
