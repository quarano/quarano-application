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

import lombok.RequiredArgsConstructor;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Encounters implements Streamable<Encounter> {

	private final List<Encounter> encounters;

	public boolean hasBeenInTouchWith(ContactPerson person, Period period) {

		var reference = LocalDate.now().minus(period);

		return true;
//		return encounters.stream() //
//				.filter(it -> it.getDate().isAfter(reference)) //
//				.anyMatch(it -> it.isEncounterWith(person));
	}

	public Optional<Encounter> havingIdOf(EncounterIdentifier id) {
		return encounters.stream() //
//				.filter(it -> it.hasId(id)) //
				.findFirst();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Encounter> iterator() {
		return encounters.iterator();
	}
}
