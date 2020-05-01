/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.department.web;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import quarano.account.Account;
import quarano.account.Department;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.validation.AlphaNumeric;
import quarano.core.validation.Alphabetic;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.Comment;
import quarano.department.InitialReport;
import quarano.department.TrackedCase;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;
import quarano.tracking.web.TrackedPersonDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class TrackedCaseRepresentations implements ExternalTrackedCaseRepresentations {

	private final MapperWrapper mapper;
	private final SmartValidator validator;
	private final MessageSourceAccessor messages;

	TrackedCaseDto toInputRepresentation(TrackedCase trackedCase) {

		var personDto = mapper.map(trackedCase.getTrackedPerson(), TrackedPersonDto.class);
		var caseDto = mapper.map(trackedCase, TrackedCaseDto.class);

		return mapper.map(personDto, caseDto);
	}

	TrackedCaseDetails toRepresentation(TrackedCase trackedCase) {

		var dto = toInputRepresentation(trackedCase);

		return new TrackedCaseDetails(trackedCase, dto, messages);
	}

	public TrackedCaseSummary toSummary(TrackedCase trackedCase) {
		return new TrackedCaseSummary(trackedCase, messages);
	}

	InitialReportDto toRepresentation(InitialReport report) {
		return mapper.map(report, InitialReportDto.class);
	}

	TrackedCase from(TrackedCase existing, TrackedCaseDto source) {

		var personSource = mapper.map(source, TrackedPersonDto.class);
		var person = mapper.map(personSource, existing.getTrackedPerson());

		return mapper.map(source, existing)
				.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate())) //
				.markEdited();
	}

	TrackedCase from(TrackedCaseDto source, Department department) {

		var personDto = mapper.map(source, TrackedPersonDto.class);
		var person = mapper.map(personDto, TrackedPerson.class);

		return mapper.map(source, new TrackedCase(person, CaseType.INDEX, department));
	}

	InitialReport from(InitialReportDto source) {
		return mapper.map(source, InitialReport.class);
	}

	InitialReport from(InitialReport existing, InitialReportDto source) {
		return source.applyTo(mapper.map(source, existing));
	}

	Comment from(CommentInput payload, Account account) {
		return new Comment(payload.getComment(), account.getFullName());
	}

	Errors validateAfterEnrollment(TrackedCaseDto source, Errors errors) {

		TrackedPersonDto dto = mapper.map(source, TrackedPersonDto.class);
		validator.validate(dto, errors);

		return errors;
	}

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	static class TrackedCaseDto {

		private @NotEmpty @Alphabetic String lastName;
		private @NotEmpty @Alphabetic String firstName;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate testDate;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate quarantineStartDate;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate quarantineEndDate;

		private @Pattern(regexp = Strings.STREET) String street;
		private @AlphaNumeric String houseNumber;
		private @Pattern(regexp = Strings.CITY) String city;
		private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
		private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
		private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
		private @Pattern(regexp = EmailAddress.PATTERN) String email;
		private @Past LocalDate dateOfBirth;

		private boolean infected;

		Errors validate(Errors errors) {

			if (!StringUtils.hasText(phone) && !StringUtils.hasText(mobilePhone)) {
				errors.rejectValue("phone", "PhoneOrMobile");
				errors.rejectValue("mobilePhone", "PhoneOrMobile");
			}

			return errors;
		}
	}

	@Relation(collectionRelation = "cases")
	static class TrackedCaseDetails extends TrackedCaseStatusAware<TrackedCaseDetails> {

		private final @Getter(onMethod = @__(@JsonUnwrapped)) TrackedCaseDto dto;

		public TrackedCaseDetails(TrackedCase trackedCase, TrackedCaseDto dto, MessageSourceAccessor messages) {

			super(trackedCase, messages);

			this.dto = dto;
		}

		public List<CommentRepresentation> getComments() {

			return getTrackedCase().getComments().stream() //
					.sorted(Comparator.comparing(Comment::getDate).reversed()) //
					.map(CommentRepresentation::new) //
					.collect(Collectors.toUnmodifiableList());
		}
	}

	@RequiredArgsConstructor
	static class CommentRepresentation {

		private final Comment comment;

		public LocalDateTime getDate() {
			return comment.getDate();
		}

		public String getComment() {
			return comment.getText();
		}

		public String getAuthor() {
			return comment.getAuthor();
		}
	}

	@Data
	static class CommentInput {
		@Textual String comment;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated({ Default.class, ValidationGroups.Index.class })
	static @interface ValidatedIndexCase {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated
	static @interface ValidatedContactCase {
	}

	static class ValidationGroups {

		interface Index {}

		interface Contact {}
	}
}
