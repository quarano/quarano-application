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
package quarano.department;

import de.wevsvirushackathon.coronareport.client.ClientRepository;
import de.wevsvirushackathon.coronareport.firstReport.FirstReportRepository;
import lombok.RequiredArgsConstructor;
import quarano.tracking.TrackedPersonRepository;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(660)
public class TrackedCaseDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final ModelMapper mapper;

	private final ClientRepository clients;
	private final TrackedPersonRepository people;
	private final DepartmentRepository departments;
	private final TrackedCaseRepository cases;

	private final FirstReportRepository firstReports;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		clients.findAll().forEach(it -> {

			var department = departments.findByName(it.getHealthDepartment().getFullName()) //
					.orElseThrow(() -> new IllegalStateException(
							String.format("Health department named %s not found!", it.getHealthDepartment().getFullName())));

			people.findByLegacyClientId(it.getClientId()) //
					.ifPresentOrElse(person -> {

						var case_ = new TrackedCase();
						case_.setTrackedPerson(person);
						case_.setDepartment(department);

						Streamable.of(firstReports.findAll()) //
								.filter(report -> report.getClient().getClientId() == it.getClientId()) //
								.stream() //
								.findFirst() //
								.map(report -> mapper.map(report, InitialReport.class)) //
								.ifPresent(report -> case_.setInitialReport(report));

						if (it.isCompletedPersonalData()) {
							case_.markEnrollmentDetailsSubmitted();
						}

						if (it.isCompletedQuestionnaire()) {
							case_.markQuestionaireSubmitted();
						}

						if (it.isCompletedContactRetro()) {
							case_.markEnrollmentContactsSubmitted();
						}

						cases.save(case_);

					}, () -> new IllegalStateException("Couldn't find tracked person with client id " + it.getClientId()));
		});
	}
}
