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

import quarano.core.QuaranoRepository;
import quarano.department.Department.DepartmentIdentifier;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
public interface TrackedCaseRepository extends QuaranoRepository<TrackedCase, TrackedCaseIdentifier> {

	Streamable<TrackedCase> findByDepartmentId(DepartmentIdentifier id);
	
	@Query("select c from TrackedCase c JOIN c.trackedPerson p WHERE c.department.id = :id ORDER BY p.lastName")
	Streamable<TrackedCase> findByDepartmentIdOrderByLastNameAsc(DepartmentIdentifier id);

	Optional<TrackedCase> findByTrackedPerson(TrackedPerson person);

	@Query("select c from TrackedCase c where c.trackedPerson.id = :identifier")
	Optional<TrackedCase> findByTrackedPerson(TrackedPersonIdentifier identifier);
}
