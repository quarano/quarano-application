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

import org.springframework.data.repository.CrudRepository;

/**
 * @author Oliver Drotbohm
 */
public interface DiaryEntryRepository extends CrudRepository<DiaryEntry, DiaryEntryIdentifier> {

	default Diary findByTrackedPerson(TrackedPerson person) {
		return findByTrackedPersonId(person.getId());
	}

	Diary findByTrackedPersonId(TrackedPersonIdentifier id);
}
