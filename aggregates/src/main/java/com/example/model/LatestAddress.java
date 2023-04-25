package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestAddress {

  private DefaultId addressId;
  private DefaultId customerId;
  private Address latest;

  @JsonCreator
  public LatestAddress(
    @JsonProperty("addressId") DefaultId addressId,
    @JsonProperty("customerId") DefaultId customerId,
    @JsonProperty("latest") Address latest
  ) {
    this.addressId = addressId;
    this.customerId = customerId;
    this.latest = latest;
  }

  public void update(
    DefaultId addressId,
    DefaultId customerId,
    Address address
  ) {
    if (EventType.DELETE == address.get_eventType()) {
      this.latest = null;
      return;
    }
    this.addressId = addressId;
    this.customerId = customerId;
    this.latest = address;
  }

  public DefaultId getAddressId() {
    return this.addressId;
  }

  public DefaultId getCustomerId() {
    return this.customerId;
  }

  public Address getLatest() {
    return this.latest;
  }

  @Override
  public String toString() {
    return (
      "LatestChild{" +
      "addressId=" +
      this.addressId +
      ", customerId=" +
      this.customerId +
      ", latest=" +
      this.latest +
      '}'
    );
  }
}
