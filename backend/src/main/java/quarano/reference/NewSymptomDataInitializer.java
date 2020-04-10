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
package quarano.reference;

import de.wevsvirushackathon.coronareport.symptomes.SymptomRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class NewSymptomDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final @NonNull SymptomRepository symptoms;
	private final @NonNull NewSymptomRepository newSymptoms;
	private final @NonNull ModelMapper mapper;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		symptoms.findAll().forEach(it -> {
			newSymptoms.save(mapper.map(it, NewSymptom.class));
		});
	}
}
