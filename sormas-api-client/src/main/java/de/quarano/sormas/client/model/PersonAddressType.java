/*
 * SORMAS REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.52.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.quarano.sormas.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets PersonAddressType
 */
public enum PersonAddressType {
  HOME("HOME"),
  PLACE_OF_RESIDENCE("PLACE_OF_RESIDENCE"),
  PLACE_OF_EXPOSURE("PLACE_OF_EXPOSURE"),
  PLACE_OF_WORK("PLACE_OF_WORK"),
  PLACE_OF_ISOLATION("PLACE_OF_ISOLATION"),
  EVENT_LOCATION("EVENT_LOCATION"),
  OTHER_ADDRESS("OTHER_ADDRESS");

  private String value;

  PersonAddressType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PersonAddressType fromValue(String text) {
    for (PersonAddressType b : PersonAddressType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
