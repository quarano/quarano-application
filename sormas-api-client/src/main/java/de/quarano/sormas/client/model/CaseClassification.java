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
 * Gets or Sets CaseClassification
 */
public enum CaseClassification {
  NOT_CLASSIFIED("NOT_CLASSIFIED"),
  SUSPECT("SUSPECT"),
  PROBABLE("PROBABLE"),
  CONFIRMED("CONFIRMED"),
  CONFIRMED_NO_SYMPTOMS("CONFIRMED_NO_SYMPTOMS"),
  CONFIRMED_UNKNOWN_SYMPTOMS("CONFIRMED_UNKNOWN_SYMPTOMS"),
  NO_CASE("NO_CASE");

  private String value;

  CaseClassification(String value) {
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
  public static CaseClassification fromValue(String text) {
    for (CaseClassification b : CaseClassification.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
