package org.scaleableandreliable.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HistoryCollect {
  @Id int id;
  String type;
  long fromDate;
  long toDate;
  boolean active;

  public HistoryCollect() {}

  public HistoryCollect(String type, long fromDate, long toDate, boolean active) {
    this.type = type;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public HistoryCollect setId(int id) {
    this.id = id;
    return this;
  }

  public String getType() {
    return type;
  }

  public HistoryCollect setType(String type) {
    this.type = type;
    return this;
  }

  public long getFromDate() {
    return fromDate;
  }

  public HistoryCollect setFromDate(long fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public long getToDate() {
    return toDate;
  }

  public HistoryCollect setToDate(long toDate) {
    this.toDate = toDate;
    return this;
  }

  public boolean isActive() {
    return active;
  }

  public HistoryCollect setActive(boolean active) {
    this.active = active;
    return this;
  }
}
