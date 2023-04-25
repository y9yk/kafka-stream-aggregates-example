package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

  private final EventType _eventType;
  private final Integer id;
  private final String firstName;
  private final String lastName;
  private final String email;

  @JsonCreator
  public Customer(
    @JsonProperty("_eventType") EventType _eventType,
    @JsonProperty("id") Integer id,
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    @JsonProperty("email") String email
  ) {
    this._eventType = _eventType != null ? _eventType : EventType.UPSERT;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public EventType get_eventType() {
    return this._eventType;
  }

  public Integer getId() {
    return this.id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  @Override
  public String toString() {
    return (
      "Customer{" +
      "_eventType='" +
      this._eventType +
      '\'' +
      ", id=" +
      this.id +
      ", first_name='" +
      this.firstName +
      '\'' +
      ", last_name='" +
      this.lastName +
      '\'' +
      ", email='" +
      this.email +
      '\'' +
      '}'
    );
  }
}
