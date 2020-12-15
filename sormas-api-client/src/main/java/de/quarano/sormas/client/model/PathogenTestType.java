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
 * Gets or Sets PathogenTestType
 */
public enum PathogenTestType {
  ANTIBODY_DETECTION("ANTIBODY_DETECTION"),
  ANTIGEN_DETECTION("ANTIGEN_DETECTION"),
  RAPID_TEST("RAPID_TEST"),
  CULTURE("CULTURE"),
  HISTOPATHOLOGY("HISTOPATHOLOGY"),
  ISOLATION("ISOLATION"),
  IGM_SERUM_ANTIBODY("IGM_SERUM_ANTIBODY"),
  IGG_SERUM_ANTIBODY("IGG_SERUM_ANTIBODY"),
  INCUBATION_TIME("INCUBATION_TIME"),
  INDIRECT_FLUORESCENT_ANTIBODY("INDIRECT_FLUORESCENT_ANTIBODY"),
  DIRECT_FLUORESCENT_ANTIBODY("DIRECT_FLUORESCENT_ANTIBODY"),
  MICROSCOPY("MICROSCOPY"),
  NEUTRALIZING_ANTIBODIES("NEUTRALIZING_ANTIBODIES"),
  PCR_RT_PCR("PCR_RT_PCR"),
  GRAM_STAIN("GRAM_STAIN"),
  LATEX_AGGLUTINATION("LATEX_AGGLUTINATION"),
  CQ_VALUE_DETECTION("CQ_VALUE_DETECTION"),
  SEQUENCING("SEQUENCING"),
  OTHER("OTHER");

  private String value;

  PathogenTestType(String value) {
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
  public static PathogenTestType fromValue(String text) {
    for (PathogenTestType b : PathogenTestType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
