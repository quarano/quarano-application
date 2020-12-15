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
 * Gets or Sets TypeOfPlace
 */
public enum TypeOfPlace {
  FACILITY("FACILITY"),
  FESTIVITIES("FESTIVITIES"),
  HOME("HOME"),
  HOSPITAL("HOSPITAL"),
  MEANS_OF_TRANSPORT("MEANS_OF_TRANSPORT"),
  PUBLIC_PLACE("PUBLIC_PLACE"),
  UNKNOWN("UNKNOWN"),
  OTHER("OTHER");

  private String value;

  TypeOfPlace(String value) {
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
  public static TypeOfPlace fromValue(String text) {
    for (TypeOfPlace b : TypeOfPlace.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
