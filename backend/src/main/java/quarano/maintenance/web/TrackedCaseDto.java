package quarano.maintenance.web;

import java.util.List;

import lombok.Data;

@Data
public class TrackedCaseDto extends QuaranoDto {
	private DepartmentDto department;
	private TestResultDto testResult;
	private QuestionnaireDto questionnaire;
	private EnrollmentDto enrollment;
	private String type;
	private QuarantineDto quarantine;
	private String extReferenceNumber;
	private List<ContactPersonDto> originContacts;
	private List<CommentDto> comments;
	private List<ActionItemDto> actions;
	private String status;
	private String newContactCaseMailStatus;
	private List<TrackedCaseDto> originCases;
}
