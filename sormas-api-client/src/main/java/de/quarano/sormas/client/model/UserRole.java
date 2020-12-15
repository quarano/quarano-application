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
 * Gets or Sets UserRole
 */
public enum UserRole {
  ADMIN("ADMIN"),
  NATIONAL_USER("NATIONAL_USER"),
  SURVEILLANCE_SUPERVISOR("SURVEILLANCE_SUPERVISOR"),
  ADMIN_SUPERVISOR("ADMIN_SUPERVISOR"),
  SURVEILLANCE_OFFICER("SURVEILLANCE_OFFICER"),
  HOSPITAL_INFORMANT("HOSPITAL_INFORMANT"),
  COMMUNITY_INFORMANT("COMMUNITY_INFORMANT"),
  CASE_SUPERVISOR("CASE_SUPERVISOR"),
  CASE_OFFICER("CASE_OFFICER"),
  CONTACT_SUPERVISOR("CONTACT_SUPERVISOR"),
  CONTACT_OFFICER("CONTACT_OFFICER"),
  EVENT_OFFICER("EVENT_OFFICER"),
  LAB_USER("LAB_USER"),
  EXTERNAL_LAB_USER("EXTERNAL_LAB_USER"),
  NATIONAL_OBSERVER("NATIONAL_OBSERVER"),
  STATE_OBSERVER("STATE_OBSERVER"),
  DISTRICT_OBSERVER("DISTRICT_OBSERVER"),
  NATIONAL_CLINICIAN("NATIONAL_CLINICIAN"),
  POE_INFORMANT("POE_INFORMANT"),
  POE_SUPERVISOR("POE_SUPERVISOR"),
  POE_NATIONAL_USER("POE_NATIONAL_USER"),
  IMPORT_USER("IMPORT_USER"),
  REST_EXTERNAL_VISITS_USER("REST_EXTERNAL_VISITS_USER"),
  REST_USER("REST_USER"),
  SORMAS_TO_SORMAS_CLIENT("SORMAS_TO_SORMAS_CLIENT");

  private String value;

  UserRole(String value) {
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
  public static UserRole fromValue(String text) {
    for (UserRole b : UserRole.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
