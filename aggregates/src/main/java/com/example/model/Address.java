package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

  private final EventType _eventType;
  private final Integer id;
  private final Integer customerId;
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String type;

  @JsonCreator
  public Address(
    @JsonProperty("_eventType") EventType _eventType,
    @JsonProperty("id") Integer id,
    @JsonProperty("customer_id") Integer customerId,
    @JsonProperty("street") String street,
    @JsonProperty("city") String city,
    @JsonProperty("state") String state,
    @JsonProperty("zip") String zip,
    @JsonProperty("type") String type
  ) {
    this._eventType = _eventType != null ? _eventType : EventType.UPSERT;
    this.id = id;
    this.customerId = customerId;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.type = type;
  }

  public EventType get_eventType() {
    return this._eventType;
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getCustomerId() {
    return this.customerId;
  }

  public String getStreet() {
    return this.street;
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }

  public String getZip() {
    return this.zip;
  }

  public String getType() {
    return this.type;
  }

  @Override
  public String toString() {
    return (
      "Address{" +
      "_eventType='" +
      this._eventType +
      '\'' +
      ", id=" +
      this.id +
      ", customer_id=" +
      this.customerId +
      ", street='" +
      this.street +
      '\'' +
      ", city='" +
      this.city +
      '\'' +
      ", state='" +
      this.state +
      '\'' +
      ", zip='" +
      this.zip +
      '\'' +
      ", type='" +
      this.type +
      '\'' +
      '}'
    );
  }
}
