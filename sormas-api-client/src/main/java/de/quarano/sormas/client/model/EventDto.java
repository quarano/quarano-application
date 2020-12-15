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
import de.quarano.sormas.client.model.EventInvestigationStatus;
import de.quarano.sormas.client.model.EventSourceType;
import de.quarano.sormas.client.model.EventStatus;
import de.quarano.sormas.client.model.LocationDto;
import de.quarano.sormas.client.model.TypeOfPlace;
import de.quarano.sormas.client.model.UserReferenceDto;
import de.quarano.sormas.client.model.YesNoUnknown;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
/**
 * EventDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-15T22:47:05.366577+01:00[Europe/Berlin]")
public class EventDto {
  @JsonProperty("creationDate")
  private OffsetDateTime creationDate = null;

  @JsonProperty("changeDate")
  private OffsetDateTime changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("pseudonymized")
  private Boolean pseudonymized = null;

  @JsonProperty("eventStatus")
  private EventStatus eventStatus = null;

  @JsonProperty("eventInvestigationStatus")
  private EventInvestigationStatus eventInvestigationStatus = null;

  @JsonProperty("eventInvestigationStartDate")
  private OffsetDateTime eventInvestigationStartDate = null;

  @JsonProperty("eventInvestigationEndDate")
  private OffsetDateTime eventInvestigationEndDate = null;

  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("eventTitle")
  private String eventTitle = null;

  @JsonProperty("eventDesc")
  private String eventDesc = null;

  @JsonProperty("nosocomial")
  private YesNoUnknown nosocomial = null;

  @JsonProperty("startDate")
  private OffsetDateTime startDate = null;

  @JsonProperty("endDate")
  private OffsetDateTime endDate = null;

  @JsonProperty("reportDateTime")
  private OffsetDateTime reportDateTime = null;

  @JsonProperty("reportingUser")
  private UserReferenceDto reportingUser = null;

  @JsonProperty("eventLocation")
  private LocationDto eventLocation = null;

  @JsonProperty("typeOfPlace")
  private TypeOfPlace typeOfPlace = null;

  @JsonProperty("srcType")
  private EventSourceType srcType = null;

  @JsonProperty("srcFirstName")
  private String srcFirstName = null;

  @JsonProperty("srcLastName")
  private String srcLastName = null;

  @JsonProperty("srcTelNo")
  private String srcTelNo = null;

  @JsonProperty("srcEmail")
  private String srcEmail = null;

  @JsonProperty("srcMediaWebsite")
  private String srcMediaWebsite = null;

  @JsonProperty("srcMediaName")
  private String srcMediaName = null;

  @JsonProperty("srcMediaDetails")
  private String srcMediaDetails = null;

  @JsonProperty("disease")
  private Disease disease = null;

  @JsonProperty("diseaseDetails")
  private String diseaseDetails = null;

  @JsonProperty("surveillanceOfficer")
  private UserReferenceDto surveillanceOfficer = null;

  @JsonProperty("typeOfPlaceText")
  private String typeOfPlaceText = null;

  @JsonProperty("reportLat")
  private Double reportLat = null;

  @JsonProperty("reportLon")
  private Double reportLon = null;

  @JsonProperty("reportLatLonAccuracy")
  private Float reportLatLonAccuracy = null;

  @JsonProperty("multiDayEvent")
  private Boolean multiDayEvent = null;

  public EventDto creationDate(OffsetDateTime creationDate) {
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

  public EventDto changeDate(OffsetDateTime changeDate) {
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

  public EventDto uuid(String uuid) {
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

  public EventDto pseudonymized(Boolean pseudonymized) {
    this.pseudonymized = pseudonymized;
    return this;
  }

   /**
   * Get pseudonymized
   * @return pseudonymized
  **/
  @Schema(description = "")
  public Boolean isPseudonymized() {
    return pseudonymized;
  }

  public void setPseudonymized(Boolean pseudonymized) {
    this.pseudonymized = pseudonymized;
  }

  public EventDto eventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
    return this;
  }

   /**
   * Get eventStatus
   * @return eventStatus
  **/
  @Schema(required = true, description = "")
  public EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }

  public EventDto eventInvestigationStatus(EventInvestigationStatus eventInvestigationStatus) {
    this.eventInvestigationStatus = eventInvestigationStatus;
    return this;
  }

   /**
   * Get eventInvestigationStatus
   * @return eventInvestigationStatus
  **/
  @Schema(description = "")
  public EventInvestigationStatus getEventInvestigationStatus() {
    return eventInvestigationStatus;
  }

  public void setEventInvestigationStatus(EventInvestigationStatus eventInvestigationStatus) {
    this.eventInvestigationStatus = eventInvestigationStatus;
  }

  public EventDto eventInvestigationStartDate(OffsetDateTime eventInvestigationStartDate) {
    this.eventInvestigationStartDate = eventInvestigationStartDate;
    return this;
  }

   /**
   * Get eventInvestigationStartDate
   * @return eventInvestigationStartDate
  **/
  @Schema(description = "")
  public OffsetDateTime getEventInvestigationStartDate() {
    return eventInvestigationStartDate;
  }

  public void setEventInvestigationStartDate(OffsetDateTime eventInvestigationStartDate) {
    this.eventInvestigationStartDate = eventInvestigationStartDate;
  }

  public EventDto eventInvestigationEndDate(OffsetDateTime eventInvestigationEndDate) {
    this.eventInvestigationEndDate = eventInvestigationEndDate;
    return this;
  }

   /**
   * Get eventInvestigationEndDate
   * @return eventInvestigationEndDate
  **/
  @Schema(description = "")
  public OffsetDateTime getEventInvestigationEndDate() {
    return eventInvestigationEndDate;
  }

  public void setEventInvestigationEndDate(OffsetDateTime eventInvestigationEndDate) {
    this.eventInvestigationEndDate = eventInvestigationEndDate;
  }

  public EventDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

   /**
   * Get externalId
   * @return externalId
  **/
  @Schema(description = "")
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public EventDto eventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
    return this;
  }

   /**
   * Get eventTitle
   * @return eventTitle
  **/
  @Schema(description = "")
  public String getEventTitle() {
    return eventTitle;
  }

  public void setEventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
  }

  public EventDto eventDesc(String eventDesc) {
    this.eventDesc = eventDesc;
    return this;
  }

   /**
   * Get eventDesc
   * @return eventDesc
  **/
  @Schema(required = true, description = "")
  public String getEventDesc() {
    return eventDesc;
  }

  public void setEventDesc(String eventDesc) {
    this.eventDesc = eventDesc;
  }

  public EventDto nosocomial(YesNoUnknown nosocomial) {
    this.nosocomial = nosocomial;
    return this;
  }

   /**
   * Get nosocomial
   * @return nosocomial
  **/
  @Schema(description = "")
  public YesNoUnknown getNosocomial() {
    return nosocomial;
  }

  public void setNosocomial(YesNoUnknown nosocomial) {
    this.nosocomial = nosocomial;
  }

  public EventDto startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @Schema(description = "")
  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public EventDto endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @Schema(description = "")
  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public EventDto reportDateTime(OffsetDateTime reportDateTime) {
    this.reportDateTime = reportDateTime;
    return this;
  }

   /**
   * Get reportDateTime
   * @return reportDateTime
  **/
  @Schema(required = true, description = "")
  public OffsetDateTime getReportDateTime() {
    return reportDateTime;
  }

  public void setReportDateTime(OffsetDateTime reportDateTime) {
    this.reportDateTime = reportDateTime;
  }

  public EventDto reportingUser(UserReferenceDto reportingUser) {
    this.reportingUser = reportingUser;
    return this;
  }

   /**
   * Get reportingUser
   * @return reportingUser
  **/
  @Schema(required = true, description = "")
  public UserReferenceDto getReportingUser() {
    return reportingUser;
  }

  public void setReportingUser(UserReferenceDto reportingUser) {
    this.reportingUser = reportingUser;
  }

  public EventDto eventLocation(LocationDto eventLocation) {
    this.eventLocation = eventLocation;
    return this;
  }

   /**
   * Get eventLocation
   * @return eventLocation
  **/
  @Schema(description = "")
  public LocationDto getEventLocation() {
    return eventLocation;
  }

  public void setEventLocation(LocationDto eventLocation) {
    this.eventLocation = eventLocation;
  }

  public EventDto typeOfPlace(TypeOfPlace typeOfPlace) {
    this.typeOfPlace = typeOfPlace;
    return this;
  }

   /**
   * Get typeOfPlace
   * @return typeOfPlace
  **/
  @Schema(description = "")
  public TypeOfPlace getTypeOfPlace() {
    return typeOfPlace;
  }

  public void setTypeOfPlace(TypeOfPlace typeOfPlace) {
    this.typeOfPlace = typeOfPlace;
  }

  public EventDto srcType(EventSourceType srcType) {
    this.srcType = srcType;
    return this;
  }

   /**
   * Get srcType
   * @return srcType
  **/
  @Schema(description = "")
  public EventSourceType getSrcType() {
    return srcType;
  }

  public void setSrcType(EventSourceType srcType) {
    this.srcType = srcType;
  }

  public EventDto srcFirstName(String srcFirstName) {
    this.srcFirstName = srcFirstName;
    return this;
  }

   /**
   * Get srcFirstName
   * @return srcFirstName
  **/
  @Schema(description = "")
  public String getSrcFirstName() {
    return srcFirstName;
  }

  public void setSrcFirstName(String srcFirstName) {
    this.srcFirstName = srcFirstName;
  }

  public EventDto srcLastName(String srcLastName) {
    this.srcLastName = srcLastName;
    return this;
  }

   /**
   * Get srcLastName
   * @return srcLastName
  **/
  @Schema(description = "")
  public String getSrcLastName() {
    return srcLastName;
  }

  public void setSrcLastName(String srcLastName) {
    this.srcLastName = srcLastName;
  }

  public EventDto srcTelNo(String srcTelNo) {
    this.srcTelNo = srcTelNo;
    return this;
  }

   /**
   * Get srcTelNo
   * @return srcTelNo
  **/
  @Schema(description = "")
  public String getSrcTelNo() {
    return srcTelNo;
  }

  public void setSrcTelNo(String srcTelNo) {
    this.srcTelNo = srcTelNo;
  }

  public EventDto srcEmail(String srcEmail) {
    this.srcEmail = srcEmail;
    return this;
  }

   /**
   * Get srcEmail
   * @return srcEmail
  **/
  @Schema(description = "")
  public String getSrcEmail() {
    return srcEmail;
  }

  public void setSrcEmail(String srcEmail) {
    this.srcEmail = srcEmail;
  }

  public EventDto srcMediaWebsite(String srcMediaWebsite) {
    this.srcMediaWebsite = srcMediaWebsite;
    return this;
  }

   /**
   * Get srcMediaWebsite
   * @return srcMediaWebsite
  **/
  @Schema(description = "")
  public String getSrcMediaWebsite() {
    return srcMediaWebsite;
  }

  public void setSrcMediaWebsite(String srcMediaWebsite) {
    this.srcMediaWebsite = srcMediaWebsite;
  }

  public EventDto srcMediaName(String srcMediaName) {
    this.srcMediaName = srcMediaName;
    return this;
  }

   /**
   * Get srcMediaName
   * @return srcMediaName
  **/
  @Schema(description = "")
  public String getSrcMediaName() {
    return srcMediaName;
  }

  public void setSrcMediaName(String srcMediaName) {
    this.srcMediaName = srcMediaName;
  }

  public EventDto srcMediaDetails(String srcMediaDetails) {
    this.srcMediaDetails = srcMediaDetails;
    return this;
  }

   /**
   * Get srcMediaDetails
   * @return srcMediaDetails
  **/
  @Schema(description = "")
  public String getSrcMediaDetails() {
    return srcMediaDetails;
  }

  public void setSrcMediaDetails(String srcMediaDetails) {
    this.srcMediaDetails = srcMediaDetails;
  }

  public EventDto disease(Disease disease) {
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

  public EventDto diseaseDetails(String diseaseDetails) {
    this.diseaseDetails = diseaseDetails;
    return this;
  }

   /**
   * Get diseaseDetails
   * @return diseaseDetails
  **/
  @Schema(description = "")
  public String getDiseaseDetails() {
    return diseaseDetails;
  }

  public void setDiseaseDetails(String diseaseDetails) {
    this.diseaseDetails = diseaseDetails;
  }

  public EventDto surveillanceOfficer(UserReferenceDto surveillanceOfficer) {
    this.surveillanceOfficer = surveillanceOfficer;
    return this;
  }

   /**
   * Get surveillanceOfficer
   * @return surveillanceOfficer
  **/
  @Schema(description = "")
  public UserReferenceDto getSurveillanceOfficer() {
    return surveillanceOfficer;
  }

  public void setSurveillanceOfficer(UserReferenceDto surveillanceOfficer) {
    this.surveillanceOfficer = surveillanceOfficer;
  }

  public EventDto typeOfPlaceText(String typeOfPlaceText) {
    this.typeOfPlaceText = typeOfPlaceText;
    return this;
  }

   /**
   * Get typeOfPlaceText
   * @return typeOfPlaceText
  **/
  @Schema(description = "")
  public String getTypeOfPlaceText() {
    return typeOfPlaceText;
  }

  public void setTypeOfPlaceText(String typeOfPlaceText) {
    this.typeOfPlaceText = typeOfPlaceText;
  }

  public EventDto reportLat(Double reportLat) {
    this.reportLat = reportLat;
    return this;
  }

   /**
   * Get reportLat
   * @return reportLat
  **/
  @Schema(description = "")
  public Double getReportLat() {
    return reportLat;
  }

  public void setReportLat(Double reportLat) {
    this.reportLat = reportLat;
  }

  public EventDto reportLon(Double reportLon) {
    this.reportLon = reportLon;
    return this;
  }

   /**
   * Get reportLon
   * @return reportLon
  **/
  @Schema(description = "")
  public Double getReportLon() {
    return reportLon;
  }

  public void setReportLon(Double reportLon) {
    this.reportLon = reportLon;
  }

  public EventDto reportLatLonAccuracy(Float reportLatLonAccuracy) {
    this.reportLatLonAccuracy = reportLatLonAccuracy;
    return this;
  }

   /**
   * Get reportLatLonAccuracy
   * @return reportLatLonAccuracy
  **/
  @Schema(description = "")
  public Float getReportLatLonAccuracy() {
    return reportLatLonAccuracy;
  }

  public void setReportLatLonAccuracy(Float reportLatLonAccuracy) {
    this.reportLatLonAccuracy = reportLatLonAccuracy;
  }

  public EventDto multiDayEvent(Boolean multiDayEvent) {
    this.multiDayEvent = multiDayEvent;
    return this;
  }

   /**
   * Get multiDayEvent
   * @return multiDayEvent
  **/
  @Schema(description = "")
  public Boolean isMultiDayEvent() {
    return multiDayEvent;
  }

  public void setMultiDayEvent(Boolean multiDayEvent) {
    this.multiDayEvent = multiDayEvent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventDto eventDto = (EventDto) o;
    return Objects.equals(this.creationDate, eventDto.creationDate) &&
        Objects.equals(this.changeDate, eventDto.changeDate) &&
        Objects.equals(this.uuid, eventDto.uuid) &&
        Objects.equals(this.pseudonymized, eventDto.pseudonymized) &&
        Objects.equals(this.eventStatus, eventDto.eventStatus) &&
        Objects.equals(this.eventInvestigationStatus, eventDto.eventInvestigationStatus) &&
        Objects.equals(this.eventInvestigationStartDate, eventDto.eventInvestigationStartDate) &&
        Objects.equals(this.eventInvestigationEndDate, eventDto.eventInvestigationEndDate) &&
        Objects.equals(this.externalId, eventDto.externalId) &&
        Objects.equals(this.eventTitle, eventDto.eventTitle) &&
        Objects.equals(this.eventDesc, eventDto.eventDesc) &&
        Objects.equals(this.nosocomial, eventDto.nosocomial) &&
        Objects.equals(this.startDate, eventDto.startDate) &&
        Objects.equals(this.endDate, eventDto.endDate) &&
        Objects.equals(this.reportDateTime, eventDto.reportDateTime) &&
        Objects.equals(this.reportingUser, eventDto.reportingUser) &&
        Objects.equals(this.eventLocation, eventDto.eventLocation) &&
        Objects.equals(this.typeOfPlace, eventDto.typeOfPlace) &&
        Objects.equals(this.srcType, eventDto.srcType) &&
        Objects.equals(this.srcFirstName, eventDto.srcFirstName) &&
        Objects.equals(this.srcLastName, eventDto.srcLastName) &&
        Objects.equals(this.srcTelNo, eventDto.srcTelNo) &&
        Objects.equals(this.srcEmail, eventDto.srcEmail) &&
        Objects.equals(this.srcMediaWebsite, eventDto.srcMediaWebsite) &&
        Objects.equals(this.srcMediaName, eventDto.srcMediaName) &&
        Objects.equals(this.srcMediaDetails, eventDto.srcMediaDetails) &&
        Objects.equals(this.disease, eventDto.disease) &&
        Objects.equals(this.diseaseDetails, eventDto.diseaseDetails) &&
        Objects.equals(this.surveillanceOfficer, eventDto.surveillanceOfficer) &&
        Objects.equals(this.typeOfPlaceText, eventDto.typeOfPlaceText) &&
        Objects.equals(this.reportLat, eventDto.reportLat) &&
        Objects.equals(this.reportLon, eventDto.reportLon) &&
        Objects.equals(this.reportLatLonAccuracy, eventDto.reportLatLonAccuracy) &&
        Objects.equals(this.multiDayEvent, eventDto.multiDayEvent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, pseudonymized, eventStatus, eventInvestigationStatus, eventInvestigationStartDate, eventInvestigationEndDate, externalId, eventTitle, eventDesc, nosocomial, startDate, endDate, reportDateTime, reportingUser, eventLocation, typeOfPlace, srcType, srcFirstName, srcLastName, srcTelNo, srcEmail, srcMediaWebsite, srcMediaName, srcMediaDetails, disease, diseaseDetails, surveillanceOfficer, typeOfPlaceText, reportLat, reportLon, reportLatLonAccuracy, multiDayEvent);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    pseudonymized: ").append(toIndentedString(pseudonymized)).append("\n");
    sb.append("    eventStatus: ").append(toIndentedString(eventStatus)).append("\n");
    sb.append("    eventInvestigationStatus: ").append(toIndentedString(eventInvestigationStatus)).append("\n");
    sb.append("    eventInvestigationStartDate: ").append(toIndentedString(eventInvestigationStartDate)).append("\n");
    sb.append("    eventInvestigationEndDate: ").append(toIndentedString(eventInvestigationEndDate)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    eventTitle: ").append(toIndentedString(eventTitle)).append("\n");
    sb.append("    eventDesc: ").append(toIndentedString(eventDesc)).append("\n");
    sb.append("    nosocomial: ").append(toIndentedString(nosocomial)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    reportDateTime: ").append(toIndentedString(reportDateTime)).append("\n");
    sb.append("    reportingUser: ").append(toIndentedString(reportingUser)).append("\n");
    sb.append("    eventLocation: ").append(toIndentedString(eventLocation)).append("\n");
    sb.append("    typeOfPlace: ").append(toIndentedString(typeOfPlace)).append("\n");
    sb.append("    srcType: ").append(toIndentedString(srcType)).append("\n");
    sb.append("    srcFirstName: ").append(toIndentedString(srcFirstName)).append("\n");
    sb.append("    srcLastName: ").append(toIndentedString(srcLastName)).append("\n");
    sb.append("    srcTelNo: ").append(toIndentedString(srcTelNo)).append("\n");
    sb.append("    srcEmail: ").append(toIndentedString(srcEmail)).append("\n");
    sb.append("    srcMediaWebsite: ").append(toIndentedString(srcMediaWebsite)).append("\n");
    sb.append("    srcMediaName: ").append(toIndentedString(srcMediaName)).append("\n");
    sb.append("    srcMediaDetails: ").append(toIndentedString(srcMediaDetails)).append("\n");
    sb.append("    disease: ").append(toIndentedString(disease)).append("\n");
    sb.append("    diseaseDetails: ").append(toIndentedString(diseaseDetails)).append("\n");
    sb.append("    surveillanceOfficer: ").append(toIndentedString(surveillanceOfficer)).append("\n");
    sb.append("    typeOfPlaceText: ").append(toIndentedString(typeOfPlaceText)).append("\n");
    sb.append("    reportLat: ").append(toIndentedString(reportLat)).append("\n");
    sb.append("    reportLon: ").append(toIndentedString(reportLon)).append("\n");
    sb.append("    reportLatLonAccuracy: ").append(toIndentedString(reportLatLonAccuracy)).append("\n");
    sb.append("    multiDayEvent: ").append(toIndentedString(multiDayEvent)).append("\n");
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
