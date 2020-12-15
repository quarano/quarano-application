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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.quarano.sormas.client.model.Disease;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
/**
 * DiseaseConfigurationDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-15T22:47:05.366577+01:00[Europe/Berlin]")
public class DiseaseConfigurationDto {
  @JsonProperty("creationDate")
  private OffsetDateTime creationDate = null;

  @JsonProperty("changeDate")
  private OffsetDateTime changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("disease")
  private Disease disease = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("primaryDisease")
  private Boolean primaryDisease = null;

  @JsonProperty("caseBased")
  private Boolean caseBased = null;

  @JsonProperty("followUpEnabled")
  private Boolean followUpEnabled = null;

  @JsonProperty("followUpDuration")
  private Integer followUpDuration = null;

  @JsonProperty("caseFollowUpDuration")
  private Integer caseFollowUpDuration = null;

  @JsonProperty("eventParticipantFollowUpDuration")
  private Integer eventParticipantFollowUpDuration = null;

  public DiseaseConfigurationDto creationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

   /**
   * Get creationDate
   * @return creationDate
  **/
  @Schema(description = "")
  public OffsetDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public DiseaseConfigurationDto changeDate(OffsetDateTime changeDate) {
    this.changeDate = changeDate;
    return this;
  }

   /**
   * Get changeDate
   * @return changeDate
  **/
  @Schema(description = "")
  public OffsetDateTime getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(OffsetDateTime changeDate) {
    this.changeDate = changeDate;
  }

  public DiseaseConfigurationDto uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @Schema(description = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public DiseaseConfigurationDto disease(Disease disease) {
    this.disease = disease;
    return this;
  }

   /**
   * Get disease
   * @return disease
  **/
  @Schema(description = "")
  public Disease getDisease() {
    return disease;
  }

  public void setDisease(Disease disease) {
    this.disease = disease;
  }

  public DiseaseConfigurationDto active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @Schema(description = "")
  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public DiseaseConfigurationDto primaryDisease(Boolean primaryDisease) {
    this.primaryDisease = primaryDisease;
    return this;
  }

   /**
   * Get primaryDisease
   * @return primaryDisease
  **/
  @Schema(description = "")
  public Boolean isPrimaryDisease() {
    return primaryDisease;
  }

  public void setPrimaryDisease(Boolean primaryDisease) {
    this.primaryDisease = primaryDisease;
  }

  public DiseaseConfigurationDto caseBased(Boolean caseBased) {
    this.caseBased = caseBased;
    return this;
  }

   /**
   * Get caseBased
   * @return caseBased
  **/
  @Schema(description = "")
  public Boolean isCaseBased() {
    return caseBased;
  }

  public void setCaseBased(Boolean caseBased) {
    this.caseBased = caseBased;
  }

  public DiseaseConfigurationDto followUpEnabled(Boolean followUpEnabled) {
    this.followUpEnabled = followUpEnabled;
    return this;
  }

   /**
   * Get followUpEnabled
   * @return followUpEnabled
  **/
  @Schema(description = "")
  public Boolean isFollowUpEnabled() {
    return followUpEnabled;
  }

  public void setFollowUpEnabled(Boolean followUpEnabled) {
    this.followUpEnabled = followUpEnabled;
  }

  public DiseaseConfigurationDto followUpDuration(Integer followUpDuration) {
    this.followUpDuration = followUpDuration;
    return this;
  }

   /**
   * Get followUpDuration
   * @return followUpDuration
  **/
  @Schema(description = "")
  public Integer getFollowUpDuration() {
    return followUpDuration;
  }

  public void setFollowUpDuration(Integer followUpDuration) {
    this.followUpDuration = followUpDuration;
  }

  public DiseaseConfigurationDto caseFollowUpDuration(Integer caseFollowUpDuration) {
    this.caseFollowUpDuration = caseFollowUpDuration;
    return this;
  }

   /**
   * Get caseFollowUpDuration
   * @return caseFollowUpDuration
  **/
  @Schema(description = "")
  public Integer getCaseFollowUpDuration() {
    return caseFollowUpDuration;
  }

  public void setCaseFollowUpDuration(Integer caseFollowUpDuration) {
    this.caseFollowUpDuration = caseFollowUpDuration;
  }

  public DiseaseConfigurationDto eventParticipantFollowUpDuration(Integer eventParticipantFollowUpDuration) {
    this.eventParticipantFollowUpDuration = eventParticipantFollowUpDuration;
    return this;
  }

   /**
   * Get eventParticipantFollowUpDuration
   * @return eventParticipantFollowUpDuration
  **/
  @Schema(description = "")
  public Integer getEventParticipantFollowUpDuration() {
    return eventParticipantFollowUpDuration;
  }

  public void setEventParticipantFollowUpDuration(Integer eventParticipantFollowUpDuration) {
    this.eventParticipantFollowUpDuration = eventParticipantFollowUpDuration;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiseaseConfigurationDto diseaseConfigurationDto = (DiseaseConfigurationDto) o;
    return Objects.equals(this.creationDate, diseaseConfigurationDto.creationDate) &&
        Objects.equals(this.changeDate, diseaseConfigurationDto.changeDate) &&
        Objects.equals(this.uuid, diseaseConfigurationDto.uuid) &&
        Objects.equals(this.disease, diseaseConfigurationDto.disease) &&
        Objects.equals(this.active, diseaseConfigurationDto.active) &&
        Objects.equals(this.primaryDisease, diseaseConfigurationDto.primaryDisease) &&
        Objects.equals(this.caseBased, diseaseConfigurationDto.caseBased) &&
        Objects.equals(this.followUpEnabled, diseaseConfigurationDto.followUpEnabled) &&
        Objects.equals(this.followUpDuration, diseaseConfigurationDto.followUpDuration) &&
        Objects.equals(this.caseFollowUpDuration, diseaseConfigurationDto.caseFollowUpDuration) &&
        Objects.equals(this.eventParticipantFollowUpDuration, diseaseConfigurationDto.eventParticipantFollowUpDuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, disease, active, primaryDisease, caseBased, followUpEnabled, followUpDuration, caseFollowUpDuration, eventParticipantFollowUpDuration);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiseaseConfigurationDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    disease: ").append(toIndentedString(disease)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    primaryDisease: ").append(toIndentedString(primaryDisease)).append("\n");
    sb.append("    caseBased: ").append(toIndentedString(caseBased)).append("\n");
    sb.append("    followUpEnabled: ").append(toIndentedString(followUpEnabled)).append("\n");
    sb.append("    followUpDuration: ").append(toIndentedString(followUpDuration)).append("\n");
    sb.append("    caseFollowUpDuration: ").append(toIndentedString(caseFollowUpDuration)).append("\n");
    sb.append("    eventParticipantFollowUpDuration: ").append(toIndentedString(eventParticipantFollowUpDuration)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
