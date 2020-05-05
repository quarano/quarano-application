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
package quarano.tracking;

import quarano.tracking.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
public interface DiaryEntryRepository extends CrudRepository<DiaryEntry, DiaryEntryIdentifier> {

	default Diary findByTrackedPerson(TrackedPerson person) {
		return findByTrackedPersonId(person.getId());
	}

	Diary findByTrackedPersonId(TrackedPersonIdentifier id);

	@Query("select distinct c.trackedPerson.id from TrackedCase c " + //
			"	where c.status = quarano.department.TrackedCase$Status.TRACKING" + //
			"	and c.trackedPerson.id not in (" + //
			"		select distinct d.trackedPersonId from DiaryEntry d where d.slot in :slots" + //
			"	)")
	Streamable<TrackedPersonIdentifier> findMissingDiaryEntryPersons(List<Slot> slots);
}
