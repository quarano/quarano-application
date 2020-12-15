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
 * Gets or Sets ContactProximity
 */
public enum ContactProximity {
  TOUCHED_FLUID("TOUCHED_FLUID"),
  PHYSICAL_CONTACT("PHYSICAL_CONTACT"),
  CLOTHES_OR_OTHER("CLOTHES_OR_OTHER"),
  CLOSE_CONTACT("CLOSE_CONTACT"),
  FACE_TO_FACE_LONG("FACE_TO_FACE_LONG"),
  MEDICAL_UNSAFE("MEDICAL_UNSAFE"),
  SAME_ROOM("SAME_ROOM"),
  AIRPLANE("AIRPLANE"),
  FACE_TO_FACE_SHORT("FACE_TO_FACE_SHORT"),
  MEDICAL_SAFE("MEDICAL_SAFE"),
  MEDICAL_SAME_ROOM("MEDICAL_SAME_ROOM"),
  AEROSOL("AEROSOL"),
  MEDICAL_DISTANT("MEDICAL_DISTANT"),
  MEDICAL_LIMITED("MEDICAL_LIMITED");

  private String value;

  ContactProximity(String value) {
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
  public static ContactProximity fromValue(String text) {
    for (ContactProximity b : ContactProximity.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
