package quarano.department.web;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quarano.department.TrackedCaseDataInitializer.*;
import static quarano.department.web.TrackedCaseCsvRepresentations.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.ZipCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.ResultHandler;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("admin")
@RequiredArgsConstructor
class TrackedCaseCsvControllerWebIntegrationTests extends AbstractDocumentation {

	private static final String QUARANTINE_CSV_HEADER = "\"" + QUARANTINE_CSV_ORDER.stream()
			.collect(Collectors.joining("\";\"")) + "\"";

	private static final String CASE_TRANSFER_CSV_HEADER = "\""
			+ TrackedCaseCsvRepresentations.CASE_TRANSFER_CSV_ORDER.stream()
					.collect(Collectors.joining("\";\""))
			+ "\"";

	private static final String CASE_TRANSFER_WITHOUT_ORIGIN_CSV_HEADER = "\""
			+ TrackedCaseCsvRepresentations.CASE_TRANSFER_CSV_ORDER.stream()
					.filter(it -> !StringUtils.equalsAny(it, LAST_CONTACT_DATE, FIRST_NAME_ORIGIN, LAST_NAME_ORIGIN,
							DATE_OF_BIRTH_ORIGIN, ZIP_CODE_ORIGIN))
					.collect(Collectors.joining("\";\""))
			+ "\"";

	private static final String SORMAS_HEADER_GROUP_LINE = "\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Location\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"Person\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Hospitalization\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"Symptoms\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"EpiData\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"HealthConditions\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"MaternalHistory\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"PortHealthInfo\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"CaseData\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"Sample\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"SormasToSormasOriginInfo\";\"Sample\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\";\"PathogenTest\"";
	private static final String SORMAS_INDEX_HEADER_LINE = "\"disease\";\"diseaseDetails\";\"plagueType\";\"dengueFeverType\";\"rabiesType\";\"person.firstName\";\"person.lastName\";\"person.nickname\";\"person.mothersName\";\"person.mothersMaidenName\";\"person.fathersName\";\"person.sex\";\"person.birthdateDD\";\"person.birthdateMM\";\"person.birthdateYYYY\";\"person.approximateAge\";\"person.approximateAgeType\";\"person.approximateAgeReferenceDate\";\"person.placeOfBirthRegion\";\"person.placeOfBirthDistrict\";\"person.placeOfBirthCommunity\";\"person.placeOfBirthFacilityType\";\"person.placeOfBirthFacility\";\"person.placeOfBirthFacilityDetails\";\"person.gestationAgeAtBirth\";\"person.birthWeight\";\"person.presentCondition\";\"person.deathDate\";\"person.causeOfDeath\";\"person.causeOfDeathDisease\";\"person.causeOfDeathDetails\";\"person.deathPlaceType\";\"person.deathPlaceDescription\";\"person.burialDate\";\"person.burialPlaceDescription\";\"person.burialConductor\";\"person.phone\";\"person.phoneOwner\";\"person.address.region\";\"person.address.district\";\"person.address.community\";\"person.address.details\";\"person.address.city\";\"person.address.areaType\";\"person.address.latitude\";\"person.address.longitude\";\"person.address.latLonAccuracy\";\"person.address.postalCode\";\"person.address.street\";\"person.address.houseNumber\";\"person.address.additionalInformation\";\"person.address.addressType\";\"person.address.addressTypeDetails\";\"person.address.facilityType\";\"person.address.facility\";\"person.address.facilityDetails\";\"person.emailAddress\";\"person.educationType\";\"person.educationDetails\";\"person.occupationType\";\"person.occupationDetails\";\"person.generalPractitionerDetails\";\"person.passportNumber\";\"person.nationalHealthId\";\"person.hasCovidApp\";\"person.covidCodeDelivered\";\"person.symptomJournalStatus\";\"person.externalId\";\"epidNumber\";\"reportDate\";\"regionLevelDate\";\"nationalLevelDate\";\"districtLevelDate\";\"caseClassification\";\"classificationDate\";\"classificationComment\";\"clinicalConfirmation\";\"epidemiologicalConfirmation\";\"laboratoryDiagnosticConfirmation\";\"investigationStatus\";\"investigatedDate\";\"outcome\";\"outcomeDate\";\"sequelae\";\"sequelaeDetails\";\"region\";\"district\";\"community\";\"facilityType\";\"healthFacility\";\"healthFacilityDetails\";\"pregnant\";\"vaccination\";\"vaccinationDoses\";\"vaccinationDate\";\"vaccinationInfoSource\";\"vaccine\";\"smallpoxVaccinationScar\";\"smallpoxVaccinationReceived\";\"clinicianName\";\"clinicianPhone\";\"clinicianEmail\";\"notifyingClinic\";\"notifyingClinicDetails\";\"reportLat\";\"reportLon\";\"reportLatLonAccuracy\";\"hospitalization.admittedToHealthFacility\";\"hospitalization.admissionDate\";\"hospitalization.dischargeDate\";\"hospitalization.isolated\";\"hospitalization.isolationDate\";\"hospitalization.leftAgainstAdvice\";\"hospitalization.intensiveCareUnit\";\"hospitalization.intensiveCareUnitStart\";\"hospitalization.intensiveCareUnitEnd\";\"symptoms.abdominalPain\";\"symptoms.anorexiaAppetiteLoss\";\"symptoms.backache\";\"symptoms.bedridden\";\"symptoms.blackeningDeathOfTissue\";\"symptoms.bleedingVagina\";\"symptoms.bloodInStool\";\"symptoms.bloodPressureDiastolic\";\"symptoms.bloodPressureSystolic\";\"symptoms.bloodUrine\";\"symptoms.bloodyBlackStool\";\"symptoms.buboesGroinArmpitNeck\";\"symptoms.bulgingFontanelle\";\"symptoms.chestPain\";\"symptoms.chillsSweats\";\"symptoms.conjunctivitis\";\"symptoms.cough\";\"symptoms.coughWithSputum\";\"symptoms.coughWithHeamoptysis\";\"symptoms.coughingBlood\";\"symptoms.darkUrine\";\"symptoms.dehydration\";\"symptoms.diarrhea\";\"symptoms.difficultyBreathing\";\"symptoms.digestedBloodVomit\";\"symptoms.eyePainLightSensitive\";\"symptoms.eyesBleeding\";\"symptoms.fatigueWeakness\";\"symptoms.fever\";\"symptoms.fluidInLungCavity\";\"symptoms.glasgowComaScale\";\"symptoms.gumsBleeding\";\"symptoms.headache\";\"symptoms.hearingloss\";\"symptoms.heartRate\";\"symptoms.height\";\"symptoms.hiccups\";\"symptoms.injectionSiteBleeding\";\"symptoms.jaundice\";\"symptoms.jaundiceWithin24HoursOfBirth\";\"symptoms.jointPain\";\"symptoms.kopliksSpots\";\"symptoms.lesions\";\"symptoms.lesionsAllOverBody\";\"symptoms.lesionsArms\";\"symptoms.lesionsDeepProfound\";\"symptoms.lesionsFace\";\"symptoms.lesionsGenitals\";\"symptoms.lesionsLegs\";\"symptoms.lesionsOnsetDate\";\"symptoms.lesionsPalmsHands\";\"symptoms.lesionsResembleImg1\";\"symptoms.lesionsResembleImg2\";\"symptoms.lesionsResembleImg3\";\"symptoms.lesionsResembleImg4\";\"symptoms.lesionsSameSize\";\"symptoms.lesionsSameState\";\"symptoms.lesionsSolesFeet\";\"symptoms.lesionsThatItch\";\"symptoms.lesionsThorax\";\"symptoms.lossSkinTurgor\";\"symptoms.lymphadenopathy\";\"symptoms.lymphadenopathyAxillary\";\"symptoms.lymphadenopathyCervical\";\"symptoms.lymphadenopathyInguinal\";\"symptoms.malaise\";\"symptoms.midUpperArmCircumference\";\"symptoms.musclePain\";\"symptoms.nausea\";\"symptoms.neckStiffness\";\"symptoms.noseBleeding\";\"symptoms.oedemaFaceNeck\";\"symptoms.oedemaLowerExtremity\";\"symptoms.onsetDate\";\"symptoms.onsetSymptom\";\"symptoms.oralUlcers\";\"symptoms.otherHemorrhagicSymptoms\";\"symptoms.otherHemorrhagicSymptomsText\";\"symptoms.otherNonHemorrhagicSymptoms\";\"symptoms.otherNonHemorrhagicSymptomsText\";\"symptoms.otitisMedia\";\"symptoms.painfulLymphadenitis\";\"symptoms.palpableLiver\";\"symptoms.palpableSpleen\";\"symptoms.pharyngealErythema\";\"symptoms.pharyngealExudate\";\"symptoms.rapidBreathing\";\"symptoms.redBloodVomit\";\"symptoms.refusalFeedorDrink\";\"symptoms.respiratoryRate\";\"symptoms.runnyNose\";\"symptoms.sidePain\";\"symptoms.skinBruising\";\"symptoms.skinRash\";\"symptoms.soreThroat\";\"symptoms.stomachBleeding\";\"symptoms.sunkenEyesFontanelle\";\"symptoms.swollenGlands\";\"symptoms.symptomsComments\";\"symptoms.temperature\";\"symptoms.temperatureSource\";\"symptoms.throbocytopenia\";\"symptoms.tremor\";\"symptoms.bilateralCataracts\";\"symptoms.unilateralCataracts\";\"symptoms.congenitalGlaucoma\";\"symptoms.pigmentaryRetinopathy\";\"symptoms.purpuricRash\";\"symptoms.microcephaly\";\"symptoms.developmentalDelay\";\"symptoms.splenomegaly\";\"symptoms.meningoencephalitis\";\"symptoms.radiolucentBoneDisease\";\"symptoms.congenitalHeartDisease\";\"symptoms.congenitalHeartDiseaseType\";\"symptoms.congenitalHeartDiseaseDetails\";\"symptoms.unexplainedBleeding\";\"symptoms.vomiting\";\"symptoms.hydrophobia\";\"symptoms.opisthotonus\";\"symptoms.anxietyStates\";\"symptoms.delirium\";\"symptoms.uproariousness\";\"symptoms.paresthesiaAroundWound\";\"symptoms.excessSalivation\";\"symptoms.insomnia\";\"symptoms.paralysis\";\"symptoms.excitation\";\"symptoms.dysphagia\";\"symptoms.aerophobia\";\"symptoms.hyperactivity\";\"symptoms.paresis\";\"symptoms.agitation\";\"symptoms.ascendingFlaccidParalysis\";\"symptoms.erraticBehaviour\";\"symptoms.coma\";\"symptoms.convulsion\";\"symptoms.fluidInLungCavityAuscultation\";\"symptoms.fluidInLungCavityXray\";\"symptoms.abnormalLungXrayFindings\";\"symptoms.conjunctivalInjection\";\"symptoms.acuteRespiratoryDistressSyndrome\";\"symptoms.pneumoniaClinicalOrRadiologic\";\"symptoms.lossOfTaste\";\"symptoms.lossOfSmell\";\"symptoms.wheezing\";\"symptoms.skinUlcers\";\"symptoms.inabilityToWalk\";\"symptoms.inDrawingOfChestWall\";\"symptoms.respiratoryDiseaseVentilation\";\"symptoms.feelingIll\";\"symptoms.fastHeartRate\";\"symptoms.oxygenSaturationLower94\";\"symptoms.weight\";\"symptoms.alteredConsciousness\";\"symptoms.confusedDisoriented\";\"symptoms.hemorrhagicSyndrome\";\"symptoms.hyperglycemia\";\"symptoms.hypoglycemia\";\"symptoms.meningealSigns\";\"symptoms.otherComplications\";\"symptoms.otherComplicationsText\";\"symptoms.seizures\";\"symptoms.sepsis\";\"symptoms.shock\";\"symptoms.feverishFeeling\";\"symptoms.weakness\";\"symptoms.fatigue\";\"symptoms.coughWithoutSputum\";\"symptoms.breathlessness\";\"symptoms.chestPressure\";\"symptoms.blueLips\";\"symptoms.bloodCirculationProblems\";\"symptoms.palpitations\";\"symptoms.dizzinessStandingUp\";\"symptoms.highOrLowBloodPressure\";\"symptoms.urinaryRetention\";\"symptoms.shivering\";\"epiData.directContactConfirmedCase\";\"epiData.directContactProbableCase\";\"epiData.closeContactProbableCase\";\"epiData.areaConfirmedCases\";\"epiData.processingConfirmedCaseFluidUnsafe\";\"epiData.percutaneousCaseBlood\";\"epiData.directContactDeadUnsafe\";\"epiData.processingSuspectedCaseSampleUnsafe\";\"epiData.areaInfectedAnimals\";\"epiData.sickDeadAnimals\";\"epiData.sickDeadAnimalsDetails\";\"epiData.sickDeadAnimalsDate\";\"epiData.sickDeadAnimalsLocation\";\"epiData.eatingRawAnimalsInInfectedArea\";\"epiData.eatingRawAnimals\";\"epiData.eatingRawAnimalsDetails\";\"epiData.rodents\";\"epiData.bats\";\"epiData.primates\";\"epiData.swine\";\"epiData.birds\";\"epiData.rabbits\";\"epiData.cattle\";\"epiData.dogs\";\"epiData.cats\";\"epiData.canidae\";\"epiData.otherAnimals\";\"epiData.otherAnimalsDetails\";\"epiData.waterSource\";\"epiData.waterSourceOther\";\"epiData.waterBody\";\"epiData.waterBodyDetails\";\"epiData.tickBite\";\"epiData.fleaBite\";\"epiData.kindOfExposureBite\";\"epiData.kindOfExposureTouch\";\"epiData.kindOfExposureScratch\";\"epiData.kindOfExposureLick\";\"epiData.kindOfExposureOther\";\"epiData.kindOfExposureDetails\";\"epiData.dateOfLastExposure\";\"epiData.placeOfLastExposure\";\"epiData.animalCondition\";\"epiData.animalVaccinationStatus\";\"epiData.prophylaxisStatus\";\"epiData.dateOfProphylaxis\";\"epiData.visitedHealthFacility\";\"epiData.contactWithSourceRespiratoryCase\";\"epiData.visitedAnimalMarket\";\"epiData.camels\";\"epiData.snakes\";\"clinicalCourse.healthConditions.tuberculosis\";\"clinicalCourse.healthConditions.asplenia\";\"clinicalCourse.healthConditions.hepatitis\";\"clinicalCourse.healthConditions.diabetes\";\"clinicalCourse.healthConditions.hiv\";\"clinicalCourse.healthConditions.hivArt\";\"clinicalCourse.healthConditions.chronicLiverDisease\";\"clinicalCourse.healthConditions.malignancyChemotherapy\";\"clinicalCourse.healthConditions.chronicHeartFailure\";\"clinicalCourse.healthConditions.chronicPulmonaryDisease\";\"clinicalCourse.healthConditions.chronicKidneyDisease\";\"clinicalCourse.healthConditions.chronicNeurologicCondition\";\"clinicalCourse.healthConditions.downSyndrome\";\"clinicalCourse.healthConditions.congenitalSyphilis\";\"clinicalCourse.healthConditions.immunodeficiencyOtherThanHiv\";\"clinicalCourse.healthConditions.cardiovascularDiseaseIncludingHypertension\";\"clinicalCourse.healthConditions.obesity\";\"clinicalCourse.healthConditions.currentSmoker\";\"clinicalCourse.healthConditions.formerSmoker\";\"clinicalCourse.healthConditions.asthma\";\"clinicalCourse.healthConditions.sickleCellDisease\";\"clinicalCourse.healthConditions.immunodeficiencyIncludingHiv\";\"clinicalCourse.healthConditions.otherConditions\";\"maternalHistory.childrenNumber\";\"maternalHistory.ageAtBirth\";\"maternalHistory.conjunctivitis\";\"maternalHistory.conjunctivitisOnset\";\"maternalHistory.conjunctivitisMonth\";\"maternalHistory.maculopapularRash\";\"maternalHistory.maculopapularRashOnset\";\"maternalHistory.maculopapularRashMonth\";\"maternalHistory.swollenLymphs\";\"maternalHistory.swollenLymphsOnset\";\"maternalHistory.swollenLymphsMonth\";\"maternalHistory.arthralgiaArthritis\";\"maternalHistory.arthralgiaArthritisOnset\";\"maternalHistory.arthralgiaArthritisMonth\";\"maternalHistory.otherComplications\";\"maternalHistory.otherComplicationsOnset\";\"maternalHistory.otherComplicationsMonth\";\"maternalHistory.otherComplicationsDetails\";\"maternalHistory.rubella\";\"maternalHistory.rubellaOnset\";\"maternalHistory.rashExposure\";\"maternalHistory.rashExposureDate\";\"maternalHistory.rashExposureMonth\";\"maternalHistory.rashExposureRegion\";\"maternalHistory.rashExposureDistrict\";\"maternalHistory.rashExposureCommunity\";\"portHealthInfo.airlineName\";\"portHealthInfo.flightNumber\";\"portHealthInfo.departureDateTime\";\"portHealthInfo.arrivalDateTime\";\"portHealthInfo.freeSeating\";\"portHealthInfo.seatNumber\";\"portHealthInfo.departureAirport\";\"portHealthInfo.numberOfTransitStops\";\"portHealthInfo.transitStopDetails1\";\"portHealthInfo.transitStopDetails2\";\"portHealthInfo.transitStopDetails3\";\"portHealthInfo.transitStopDetails4\";\"portHealthInfo.transitStopDetails5\";\"portHealthInfo.vesselName\";\"portHealthInfo.vesselDetails\";\"portHealthInfo.portOfDeparture\";\"portHealthInfo.lastPortOfCall\";\"portHealthInfo.conveyanceType\";\"portHealthInfo.conveyanceTypeDetails\";\"portHealthInfo.departureLocation\";\"portHealthInfo.finalDestination\";\"portHealthInfo.details\";\"caseOrigin\";\"pointOfEntry\";\"pointOfEntryDetails\";\"additionalDetails\";\"externalID\";\"sharedToCountry\";\"quarantine\";\"quarantineTypeDetails\";\"quarantineFrom\";\"quarantineTo\";\"quarantineHelpNeeded\";\"quarantineOrderedVerbally\";\"quarantineOrderedOfficialDocument\";\"quarantineOrderedVerballyDate\";\"quarantineOrderedOfficialDocumentDate\";\"quarantineHomePossible\";\"quarantineHomePossibleComment\";\"quarantineHomeSupplyEnsured\";\"quarantineHomeSupplyEnsuredComment\";\"quarantineExtended\";\"quarantineReduced\";\"quarantineOfficialOrderSent\";\"quarantineOfficialOrderSentDate\";\"reportingType\";\"postpartum\";\"trimester\";\"followUpStatus\";\"followUpComment\";\"followUpUntil\";\"overwriteFollowUpUntil\";\"sormasToSormasOriginInfo.organizationId\";\"sormasToSormasOriginInfo.senderName\";\"sormasToSormasOriginInfo.senderEmail\";\"sormasToSormasOriginInfo.senderPhoneNumber\";\"sormasToSormasOriginInfo.ownershipHandedOver\";\"sormasToSormasOriginInfo.comment\";\"ownershipHandedOver\";\"caseIdIsm\";\"covidTestReason\";\"covidTestReasonDetails\";\"contactTracingFirstContactType\";\"contactTracingFirstContactDate\";\"wasInQuarantineBeforeIsolation\";\"quarantineReasonBeforeIsolation\";\"quarantineReasonBeforeIsolationDetails\";\"endOfIsolationReason\";\"endOfIsolationReasonDetails\";\"labSampleID\";\"fieldSampleID\";\"sampleDateTime\";\"reportDateTime\";\"sampleMaterial\";\"sampleMaterialText\";\"samplePurpose\";\"lab\";\"labDetails\";\"shipmentDate\";\"shipmentDetails\";\"receivedDate\";\"specimenCondition\";\"noTestPossibleReason\";\"comment\";\"sampleSource\";\"shipped\";\"received\";\"pathogenTestResult\";\"sormasToSormasOriginInfo.organizationId\";\"sormasToSormasOriginInfo.senderName\";\"sormasToSormasOriginInfo.senderEmail\";\"sormasToSormasOriginInfo.senderPhoneNumber\";\"sormasToSormasOriginInfo.ownershipHandedOver\";\"sormasToSormasOriginInfo.comment\";\"ownershipHandedOver\";\"testedDisease\";\"testedDiseaseDetails\";\"testType\";\"testTypeText\";\"testDateTime\";\"lab\";\"labDetails\";\"testResult\";\"testResultText\";\"testResultVerified\";\"fourFoldIncreaseAntibodyTiter\";\"serotype\";\"cqValue\"";
	private static final String SORMAS_CONTACT_HEADER_LINE = "\"caseIdExternalSystem\";\"caseOrEventInformation\";\"disease\";\"diseaseDetails\";\"reportDateTime\";\"reportLat\";\"reportLon\";\"reportLatLonAccuracy\";\"region\";\"district\";\"community\";\"lastContactDate\";\"contactIdentificationSource\";\"contactIdentificationSourceDetails\";\"tracingApp\";\"tracingAppDetails\";\"contactProximity\";\"contactProximityDetails\";\"contactCategory\";\"contactClassification\";\"contactStatus\";\"followUpStatus\";\"followUpComment\";\"followUpUntil\";\"overwriteFollowUpUntil\";\"description\";\"relationToCase\";\"relationDescription\";\"externalID\";\"highPriority\";\"immunosuppressiveTherapyBasicDisease\";\"immunosuppressiveTherapyBasicDiseaseDetails\";\"careForPeopleOver60\";\"quarantine\";\"quarantineTypeDetails\";\"quarantineFrom\";\"quarantineTo\";\"person.firstName\";\"person.lastName\";\"person.nickname\";\"person.mothersName\";\"person.mothersMaidenName\";\"person.fathersName\";\"person.sex\";\"person.birthdateDD\";\"person.birthdateMM\";\"person.birthdateYYYY\";\"person.approximateAge\";\"person.approximateAgeType\";\"person.approximateAgeReferenceDate\";\"person.placeOfBirthRegion\";\"person.placeOfBirthDistrict\";\"person.placeOfBirthCommunity\";\"person.placeOfBirthFacilityType\";\"person.placeOfBirthFacility\";\"person.placeOfBirthFacilityDetails\";\"person.gestationAgeAtBirth\";\"person.birthWeight\";\"person.presentCondition\";\"person.deathDate\";\"person.causeOfDeath\";\"person.causeOfDeathDisease\";\"person.causeOfDeathDetails\";\"person.deathPlaceType\";\"person.deathPlaceDescription\";\"person.burialDate\";\"person.burialPlaceDescription\";\"person.burialConductor\";\"person.phone\";\"person.phoneOwner\";\"person.address.region\";\"person.address.district\";\"person.address.community\";\"person.address.details\";\"person.address.city\";\"person.address.areaType\";\"person.address.latitude\";\"person.address.longitude\";\"person.address.latLonAccuracy\";\"person.address.postalCode\";\"person.address.street\";\"person.address.houseNumber\";\"person.address.additionalInformation\";\"person.address.addressType\";\"person.address.addressTypeDetails\";\"person.address.facilityType\";\"person.address.facility\";\"person.address.facilityDetails\";\"person.emailAddress\";\"person.educationType\";\"person.educationDetails\";\"person.occupationType\";\"person.occupationDetails\";\"person.generalPractitionerDetails\";\"person.passportNumber\";\"person.nationalHealthId\";\"person.hasCovidApp\";\"person.covidCodeDelivered\";\"person.symptomJournalStatus\";\"person.externalId\";\"quarantineHelpNeeded\";\"quarantineOrderedVerbally\";\"quarantineOrderedOfficialDocument\";\"quarantineOrderedVerballyDate\";\"quarantineOrderedOfficialDocumentDate\";\"quarantineHomePossible\";\"quarantineHomePossibleComment\";\"quarantineHomeSupplyEnsured\";\"quarantineHomeSupplyEnsuredComment\";\"quarantineExtended\";\"quarantineReduced\";\"quarantineOfficialOrderSent\";\"quarantineOfficialOrderSentDate\";\"additionalDetails\";\"epiData.directContactConfirmedCase\";\"epiData.directContactProbableCase\";\"epiData.closeContactProbableCase\";\"epiData.areaConfirmedCases\";\"epiData.processingConfirmedCaseFluidUnsafe\";\"epiData.percutaneousCaseBlood\";\"epiData.directContactDeadUnsafe\";\"epiData.processingSuspectedCaseSampleUnsafe\";\"epiData.areaInfectedAnimals\";\"epiData.sickDeadAnimals\";\"epiData.sickDeadAnimalsDetails\";\"epiData.sickDeadAnimalsDate\";\"epiData.sickDeadAnimalsLocation\";\"epiData.eatingRawAnimalsInInfectedArea\";\"epiData.eatingRawAnimals\";\"epiData.eatingRawAnimalsDetails\";\"epiData.rodents\";\"epiData.bats\";\"epiData.primates\";\"epiData.swine\";\"epiData.birds\";\"epiData.rabbits\";\"epiData.cattle\";\"epiData.dogs\";\"epiData.cats\";\"epiData.canidae\";\"epiData.otherAnimals\";\"epiData.otherAnimalsDetails\";\"epiData.waterSource\";\"epiData.waterSourceOther\";\"epiData.waterBody\";\"epiData.waterBodyDetails\";\"epiData.tickBite\";\"epiData.fleaBite\";\"epiData.kindOfExposureBite\";\"epiData.kindOfExposureTouch\";\"epiData.kindOfExposureScratch\";\"epiData.kindOfExposureLick\";\"epiData.kindOfExposureOther\";\"epiData.kindOfExposureDetails\";\"epiData.dateOfLastExposure\";\"epiData.placeOfLastExposure\";\"epiData.animalCondition\";\"epiData.animalVaccinationStatus\";\"epiData.prophylaxisStatus\";\"epiData.dateOfProphylaxis\";\"epiData.visitedHealthFacility\";\"epiData.contactWithSourceRespiratoryCase\";\"epiData.visitedAnimalMarket\";\"epiData.camels\";\"epiData.snakes\";\"healthConditions.tuberculosis\";\"healthConditions.asplenia\";\"healthConditions.hepatitis\";\"healthConditions.diabetes\";\"healthConditions.hiv\";\"healthConditions.hivArt\";\"healthConditions.chronicLiverDisease\";\"healthConditions.malignancyChemotherapy\";\"healthConditions.chronicHeartFailure\";\"healthConditions.chronicPulmonaryDisease\";\"healthConditions.chronicKidneyDisease\";\"healthConditions.chronicNeurologicCondition\";\"healthConditions.downSyndrome\";\"healthConditions.congenitalSyphilis\";\"healthConditions.immunodeficiencyOtherThanHiv\";\"healthConditions.cardiovascularDiseaseIncludingHypertension\";\"healthConditions.obesity\";\"healthConditions.currentSmoker\";\"healthConditions.formerSmoker\";\"healthConditions.asthma\";\"healthConditions.sickleCellDisease\";\"healthConditions.immunodeficiencyIncludingHiv\";\"healthConditions.otherConditions\";\"sormasToSormasOriginInfo.organizationId\";\"sormasToSormasOriginInfo.senderName\";\"sormasToSormasOriginInfo.senderEmail\";\"sormasToSormasOriginInfo.senderPhoneNumber\";\"sormasToSormasOriginInfo.ownershipHandedOver\";\"sormasToSormasOriginInfo.comment\";\"ownershipHandedOver\";\"returningTraveler\";\"endOfQuarantineReason\";\"endOfQuarantineReasonDetails\"";

	private final @NonNull TrackedCaseRepository cases;

	@TestFactory
	Stream<DynamicTest> getQuarantineOrdersSuccessful() throws Exception {

		var now = LocalDate.now();

		var source = Stream.of(
				new ValidOrder(null, null, null, 15, false),
				new ValidOrder("index", null, null, 15, false),
				new ValidOrder("contact", null, null, 0, false),
				new ValidOrder("wrongType", null, null, 15, false),
				new ValidOrder("index", now, null, 15, false),
				new ValidOrder("index", now.plusDays(1), null, 0, false),
				new ValidOrder("index", null, now, 15, false),
				new ValidOrder("index", null, now.minusDays(1), 0, false),
				new ValidOrder("index", now, now, 15, false),
				new ValidOrder("index", now.minusDays(1), now.plusDays(1), 15, true),
				new ValidOrder("index", now.plusDays(1), now.minusDays(1), 0, false));

		return DynamicTest.stream(source.iterator(), Object::toString, bar -> {

			var result = mvc.perform(get("/hd/quarantines")
					.accept(MediaType.valueOf("text/csv"))
					.param("type", bar.getType())
					.param("from", bar.getFrom())
					.param("to", bar.getTo()))
					.andDo(documentGetQuarantines(bar.isDocument()))
					.andExpect(status().is2xxSuccessful())
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
					.andReturn().getResponse().getContentAsString();

			assertThat(result.lines()).size().isEqualTo(bar.getResultSize());

			if (bar.getResultSize() != 0) {
				assertThat(result.lines()).first().isEqualTo(QUARANTINE_CSV_HEADER);
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> getQuarantineOrderWith400() throws Exception {

		var sources = Stream.of(
				new InvalidOrder(null, LocalDate.now(), null),
				new InvalidOrder(null, null, LocalDate.now()));

		return DynamicTest.stream(sources.iterator(), Object::toString, foo -> {

			mvc.perform(get("/hd/quarantines")
					.accept(MediaType.valueOf("text/csv"))
					.param("type", foo.getType())
					.param("from", foo.getFrom())
					.param("to", foo.getTo()))
					.andExpect(status().is4xxClientError());
		});
	}

	@Test
	void getQuarantinesForIds() throws Exception {

		var ids = "[\n\"" + String.join("\",\"", TRACKED_CASE_SANDRA.toString(), TRACKED_CASE_TANJA.toString()) + "\"\n]";

		var result = mvc.perform(get("/hd/quarantinesForIds")
				.accept(MediaType.valueOf("text/csv"))
				.content(ids))
				.andDo(documentGetQuarantinesForIds())
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
				.andReturn().getResponse().getContentAsString();

		assertThat(result.lines()).size().isEqualTo(3);
		assertThat(result.lines()).first().isEqualTo(QUARANTINE_CSV_HEADER);
	}

	@TestFactory
	Stream<DynamicTest> getExternalCasesSuccessful() throws Exception {

		cases.findById(TRACKED_CASE_GUSTAV).map(it -> {

			it.getTrackedPerson().getAddress().setZipCode(ZipCode.of("01665"));

			return it;
		}).map(TrackedCase::markAsExternalZip).map(cases::save).orElseThrow();

		var now = LocalDate.now();

		var source = Stream.of(
				new ValidExternalCase(null, null, 2, false),
				new ValidExternalCase(now, null, 2, false),
				new ValidExternalCase(now.plusDays(1), null, 0, false),
				new ValidExternalCase(null, now, 2, false),
				new ValidExternalCase(null, now.minusDays(1), 0, false),
				new ValidExternalCase(now, now, 2, false),
				new ValidExternalCase(now.minusDays(1), now.plusDays(1), 2, true),
				new ValidExternalCase(now.plusDays(1), now.minusDays(1), 0, false));

		return DynamicTest.stream(source.iterator(), Object::toString, bar -> {

			var result = mvc.perform(get("/hd/externalcases")
					.param("from", bar.getFrom())
					.param("to", bar.getTo()))
					.andDo(documentGetExternalCases(bar.isDocument()))
					.andExpect(status().is2xxSuccessful())
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("application/hal+json")))
					.andReturn().getResponse().getContentAsString();

			if (bar.getResultSize() != 0) {

				var document = JsonPath.parse(result);

				assertThat(document.read("$._embedded.departmentCsvGroups[0].rkiCode", String.class))
						.isEqualTo("1.14.6.27.01.");

				var csv = document.read("$._embedded.departmentCsvGroups[0].casesCsv", String.class);

				assertThat(csv.lines()).size().isEqualTo(bar.getResultSize());
				assertThat(csv.lines()).first().isEqualTo(CASE_TRANSFER_CSV_HEADER);
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> getExternalCasesWith400() throws Exception {

		var sources = Stream.of(
				new InvalidExternalCase(LocalDate.now(), null),
				new InvalidExternalCase(null, LocalDate.now()));

		return DynamicTest.stream(sources.iterator(), Object::toString, foo -> {

			mvc.perform(get("/hd/externalcases")
					.param("from", foo.getFrom())
					.param("to", foo.getTo()))
					.andExpect(status().is4xxClientError());
		});
	}

	@TestFactory
	Stream<DynamicTest> getCasesSuccessful() throws Exception {

		cases.findById(TRACKED_CASE_GUSTAV).map(TrackedCase::markAsExternalZip).map(cases::save).orElseThrow();

		var now = LocalDate.now();

		var source = Stream.of(
				new ValidCase(null, null, null, null, null, null, 22, false),
				new ValidCase("index", null, null, null, null, null, 14, false),
				new ValidCase("contact", null, null, null, null, null, 9, false),
				new ValidCase("wrongType", null, null, null, null, null, 22, false),
				new ValidCase("index", "OPEN", null, null, null, null, 8, false),
				new ValidCase("index", "wrongStatus", null, null, null, null, 14, false),
				new ValidCase("index", null, "false", null, null, null, 15, false),
				new ValidCase("index", null, null, "true", null, null, 14, false),
				new ValidCase("index", null, null, "false", null, null, 14, false),
				new ValidCase("index", null, null, null, now, null, 14, false),
				new ValidCase("index", null, null, null, now.plusDays(1), null, 0, false),
				new ValidCase("index", null, null, null, null, now, 14, false),
				new ValidCase("index", null, null, null, null, now.minusDays(1), 0, false),
				new ValidCase("index", null, null, null, now, now, 14, false),
				new ValidCase("index", null, null, null, now.minusDays(1), now.plusDays(1), 14, false),
				new ValidCase("index", "OPEN", "false", "true", now.minusDays(1), now.plusDays(1), 8, true),
				new ValidCase("index", null, null, null, now.plusDays(1), now.minusDays(1), 0, false));

		return DynamicTest.stream(source.iterator(), Object::toString, bar -> {

			var result = mvc.perform(get("/hd/cases")
					.accept(MediaType.valueOf("text/csv"))
					.param("type", bar.getType())
					.param("status", bar.getStatus())
					.param("withorigincase", bar.getWithOriginCase())
					.param("withoutexternalcases", bar.getWithoutExternalCases())
					.param("from", bar.getFrom())
					.param("to", bar.getTo()))
					.andDo(documentGetCases(bar.isDocument()))
					.andExpect(status().is2xxSuccessful())
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
					.andReturn().getResponse().getContentAsString();

			assertThat(result.lines()).size().isEqualTo(bar.getResultSize());

			if (bar.getResultSize() != 0) {
				assertThat(result.lines()).first().isEqualTo(StringUtils.equals(bar.getWithOriginCase(), "true")
						? CASE_TRANSFER_CSV_HEADER
						: CASE_TRANSFER_WITHOUT_ORIGIN_CSV_HEADER);
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> getCasesWith400() throws Exception {

		var sources = Stream.of(
				new InvalidCase(null, null, null, null, LocalDate.now(), null),
				new InvalidCase(null, null, null, null, null, LocalDate.now()),
				new InvalidCase(null, null, "wrongBoolean", null, null, null));

		return DynamicTest.stream(sources.iterator(), Object::toString, foo -> {

			mvc.perform(get("/hd/cases")
					.accept(MediaType.valueOf("text/csv"))
					.param("type", foo.getType())
					.param("status", foo.getStatus())
					.param("withorigincase", foo.getWithoutExternalCases())
					.param("withoutexternalcases", foo.getWithoutExternalCases())
					.param("from", foo.getFrom())
					.param("to", foo.getTo()))
					.andExpect(status().is4xxClientError());
		});
	}

	@Test
	void getCasesForIds() throws Exception {

		var ids = "[\n\"" + String.join("\",\"", TRACKED_CASE_SANDRA.toString(), TRACKED_CASE_TANJA.toString()) + "\"\n]";

		var result = mvc.perform(get("/hd/casesForIds")
				.accept(MediaType.valueOf("text/csv"))
				.param("withorigincase", "true")
				.content(ids))
				.andDo(documentGetCasesForIds())
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
				.andReturn().getResponse().getContentAsString();

		assertThat(result.lines()).size().isEqualTo(3);
		assertThat(result.lines()).first().isEqualTo(CASE_TRANSFER_CSV_HEADER);
	}

	@TestFactory
	Stream<DynamicTest> getSormasCsv() throws Exception {

		cases.findById(TRACKED_CASE_GUSTAV).map(it -> it.setSormasCaseId("Sormas-ID")).map(cases::save).orElseThrow();

		var now = LocalDate.now();

		var source = Stream.of(
				new ValidSormas("index", null, null, null, 16, false),
				new ValidSormas("contact", null, null, null, 9, false),
				new ValidSormas("index", "true", null, null, 15, false),
				new ValidSormas("index", "false", null, null, 16, false),
				new ValidSormas("index", null, now, null, 16, false),
				new ValidSormas("index", null, now.plusDays(1), null, 0, false),
				new ValidSormas("index", null, null, now, 16, false),
				new ValidSormas("index", null, null, now.minusDays(1), 0, false),
				new ValidSormas("index", null, now, now, 16, false),
				new ValidSormas("index", "true", now.minusDays(1), now.plusDays(1), 15, true),
				new ValidSormas("index", null, now.plusDays(1), now.minusDays(1), 0, false));

		return DynamicTest.stream(source.iterator(), Object::toString, bar -> {

			var result = mvc.perform(get("/hd/sormas")
					.contentType(MediaType.valueOf("text/csv"))
					.param("createdfrom", bar.getFrom())
					.param("createdto", bar.getTo())
					.param("onlywithoutsormasid", bar.getOnlyWithoutSormasId())
					.param("type", bar.getType()))
					.andDo(documentGetSormasCsv(bar.isDocument()))
					.andExpect(status().is2xxSuccessful())
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
					.andReturn().getResponse().getContentAsString();

			assertThat(result.lines()).size().isEqualTo(bar.getResultSize());

			if (bar.getResultSize() != 0) {

				var lines = result.lines().toArray(String[]::new);

				if (bar.getType().equals("index")) {
					assertThat(lines[0].split(";")).allMatch(SORMAS_HEADER_GROUP_LINE::contains);
					assertThat(lines[1].split(";")).allMatch(SORMAS_INDEX_HEADER_LINE::contains);
				} else if (bar.getType().equals("contact")) {
					assertThat(lines[0].split(";")).allMatch(SORMAS_CONTACT_HEADER_LINE::contains);
				} else {
					throw new IllegalArgumentException("Case type is wrong!");
				}
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> getSormasCsvWithErrors() throws Exception {

		var sources = Stream.of(
				new InvalidSormas(null, null, null, null),
				new InvalidSormas("wrongType", null, null, null),
				new InvalidSormas("index", null, LocalDate.now(), null),
				new InvalidSormas("index", null, null, LocalDate.now()));

		return DynamicTest.stream(sources.iterator(), Object::toString, foo -> {

			mvc.perform(get("/hd/sormas")
					.contentType(MediaType.valueOf("text/csv;charset=UTF-8"))
					.param("createdfrom", foo.getType())
					.param("createdto", foo.getFrom())
					.param("onlywithoutsormasid", foo.getTo())
					.param("type", foo.getType()))
					.andExpect(status().is4xxClientError());
		});
	}

	@Test
	void getSormasCsvForIdsWith400ForNotUniqueTypes() throws Exception {

		var ids = "[\n\"" + String.join("\",\"", TRACKED_CASE_SANDRA.toString(), TRACKED_CASE_TANJA.toString()) + "\"\n]";

		mvc.perform(get("/hd/sormasForIds")
				.accept(MediaType.valueOf("text/csv"))
				.content(ids))
				.andDo(documentGetQuarantinesForIds())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void getSormasCsvForIds() throws Exception {

		var ids = "[\n\"" + String.join("\",\"", TRACKED_CASE_SANDRA.toString(), TRACKED_CASE_SIGGI.toString()) + "\"\n]";

		var result = mvc.perform(get("/hd/sormasForIds")
				.accept(MediaType.valueOf("text/csv"))
				.content(ids))
				.andDo(documentGetSormasForIds())
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
				.andReturn().getResponse().getContentAsString();

		assertThat(result.lines()).size().isEqualTo(4);

		var lines = result.lines().toArray(String[]::new);

		assertThat(lines[0].split(";")).allMatch(SORMAS_HEADER_GROUP_LINE::contains);
		assertThat(lines[1].split(";")).allMatch(SORMAS_INDEX_HEADER_LINE::contains);

		ids = "[\n\"" + String.join("\",\"", TRACKED_CASE_TANJA.toString()) + "\"\n]";

		result = mvc.perform(get("/hd/sormasForIds")
				.accept(MediaType.valueOf("text/csv"))
				.content(ids))
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
				.andReturn().getResponse().getContentAsString();

		assertThat(result.lines()).size().isEqualTo(2);

		lines = result.lines().toArray(String[]::new);

		assertThat(lines[0].split(";")).allMatch(SORMAS_CONTACT_HEADER_LINE::contains);
	}

	@Test
	void getTemplate() throws Exception {

		var result = mvc.perform(get("/admin/cases/template")
				.accept(MediaType.valueOf("text/csv")))
				.andDo(documentGetTemplate())
				.andExpect(status().is2xxSuccessful())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv;charset=UTF-8")))
				.andReturn().getResponse().getContentAsString();

		assertThat(result.lines()).size().isEqualTo(2);
		assertThat(result.lines()).first().isEqualTo(CASE_TRANSFER_WITHOUT_ORIGIN_CSV_HEADER);
	}

	private static ResultHandler documentGetQuarantines(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("quarantine-order")
				: result -> {};
	}

	private static ResultHandler documentGetQuarantinesForIds() {
		return DocumentationFlow.of("csv-import-export").document("quarantine-order-for-ids");
	}

	private static ResultHandler documentGetExternalCases(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("external-cases")
				: result -> {};
	}

	private static ResultHandler documentGetCases(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("cases")
				: result -> {};
	}

	private static ResultHandler documentGetCasesForIds() {
		return DocumentationFlow.of("csv-import-export").document("cases-for-ids");
	}

	private static ResultHandler documentGetSormasCsv(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("sormas-csv")
				: result -> {};
	}

	private static ResultHandler documentGetSormasForIds() {
		return DocumentationFlow.of("csv-import-export").document("sormas-csv-for-ids");
	}

	private static ResultHandler documentGetTemplate() {
		return DocumentationFlow.of("csv-import-export").document("template");
	}

	@Getter
	@AllArgsConstructor
	private static class InvalidOrder {

		@Nullable
		String type;
		@Nullable
		LocalDate from, to;

		@Nullable
		String getFrom() {
			return toNullableString(from);
		}

		@Nullable
		String getTo() {
			return toNullableString(to);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("Get quarantine order for type %s from %s to %s", type, from, to);
		}

		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		}
	}

	@Getter
	private static class ValidOrder extends InvalidOrder {

		int resultSize;
		boolean document;

		public ValidOrder(@Nullable String type, @Nullable LocalDate from, @Nullable LocalDate to, int resultSize,
				boolean document) {

			super(type, from, to);

			this.resultSize = resultSize;
			this.document = document;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.department.web.TrackedCaseCsvControllerWebIntegrationTests.Foo#toNullableString(java.time.LocalDate)
		 */
		@Override
		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(ISO_DATE);
		}
	}

	@Getter
	@AllArgsConstructor
	private static class InvalidCase {

		@Nullable
		String type, status, withoutExternalCases, withOriginCase;
		@Nullable
		LocalDate from, to;

		@Nullable
		String getFrom() {
			return toNullableString(from);
		}

		@Nullable
		String getTo() {
			return toNullableString(to);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("Get cases for type %s status %s from %s to %s withoutExternalCases %s withOriginCase %s",
					type, status, from, to, withoutExternalCases, withOriginCase);
		}

		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		}
	}

	@Getter
	private static class ValidCase extends InvalidCase {

		int resultSize;
		boolean document;

		public ValidCase(@Nullable String type, @Nullable String status, @Nullable String withoutExternalCases,
				@Nullable String withOriginCase,
				@Nullable LocalDate from, @Nullable LocalDate to, int resultSize, boolean document) {

			super(type, status, withoutExternalCases, withOriginCase, from, to);

			this.resultSize = resultSize;
			this.document = document;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.department.web.TrackedCaseCsvControllerWebIntegrationTests.Foo#toNullableString(java.time.LocalDate)
		 */
		@Override
		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(ISO_DATE);
		}
	}

	@Getter
	@AllArgsConstructor
	private static class InvalidSormas {

		@Nullable
		String type, onlyWithoutSormasId;
		@Nullable
		LocalDate from, to;

		@Nullable
		String getFrom() {
			return toNullableString(from);
		}

		@Nullable
		String getTo() {
			return toNullableString(to);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format(
					"Get sormas cases for type %s createdFrom %s createdTo %s onlyWithoutSormasId %s", type, from,
					to, onlyWithoutSormasId);
		}

		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		}
	}

	@Getter
	private static class ValidSormas extends InvalidSormas {

		int resultSize;
		boolean document;

		public ValidSormas(@Nullable String type, @Nullable String onlyWithoutSormasId, @Nullable LocalDate from,
				@Nullable LocalDate to, int resultSize, boolean document) {

			super(type, onlyWithoutSormasId, from, to);

			this.resultSize = resultSize;
			this.document = document;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.department.web.TrackedCaseCsvControllerWebIntegrationTests.Foo#toNullableString(java.time.LocalDate)
		 */
		@Override
		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(ISO_DATE);
		}
	}

	@Getter
	@AllArgsConstructor
	private static class InvalidExternalCase {

		@Nullable
		LocalDate from, to;

		@Nullable
		String getFrom() {
			return toNullableString(from);
		}

		@Nullable
		String getTo() {
			return toNullableString(to);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("Get external cases for modifiedFrom %s modifiedTo %s", from, to);
		}

		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		}
	}

	@Getter
	private static class ValidExternalCase extends InvalidExternalCase {

		int resultSize;
		boolean document;

		public ValidExternalCase(@Nullable LocalDate from, @Nullable LocalDate to, int resultSize, boolean document) {

			super(from, to);

			this.resultSize = resultSize;
			this.document = document;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.department.web.TrackedCaseCsvControllerWebIntegrationTests.Foo#toNullableString(java.time.LocalDate)
		 */
		@Override
		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(ISO_DATE);
		}
	}
}
