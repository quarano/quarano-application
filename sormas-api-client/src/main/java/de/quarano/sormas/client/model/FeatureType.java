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
 * Gets or Sets FeatureType
 */
public enum FeatureType {
  LINE_LISTING("LINE_LISTING"),
  AGGREGATE_REPORTING("AGGREGATE_REPORTING"),
  EVENT_SURVEILLANCE("EVENT_SURVEILLANCE"),
  WEEKLY_REPORTING("WEEKLY_REPORTING"),
  CLINICAL_MANAGEMENT("CLINICAL_MANAGEMENT"),
  NATIONAL_CASE_SHARING("NATIONAL_CASE_SHARING"),
  TASK_GENERATION_CASE_SURVEILLANCE("TASK_GENERATION_CASE_SURVEILLANCE"),
  TASK_GENERATION_CONTACT_TRACING("TASK_GENERATION_CONTACT_TRACING"),
  TASK_GENERATION_EVENT_SURVEILLANCE("TASK_GENERATION_EVENT_SURVEILLANCE"),
  TASK_GENERATION_GENERAL("TASK_GENERATION_GENERAL"),
  CAMPAIGNS("CAMPAIGNS"),
  CASE_SURVEILANCE("CASE_SURVEILANCE"),
  CONTACT_TRACING("CONTACT_TRACING"),
  SAMPLES_LAB("SAMPLES_LAB"),
  INFRASTRUCTURE_TYPE_AREA("INFRASTRUCTURE_TYPE_AREA"),
  CASE_FOLLOWUP("CASE_FOLLOWUP"),
  TASK_NOTIFICATIONS("TASK_NOTIFICATIONS"),
  OTHER_NOTIFICATIONS("OTHER_NOTIFICATIONS"),
  DOCUMENTS("DOCUMENTS");

  private String value;

  FeatureType(String value) {
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
  public static FeatureType fromValue(String text) {
    for (FeatureType b : FeatureType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
