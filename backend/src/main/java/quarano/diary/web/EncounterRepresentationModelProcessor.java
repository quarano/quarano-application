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
package quarano.diary.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryManagement;
import quarano.tracking.web.EncounterDto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link RepresentationModelProcessor} to enrich {@link EncounterDto} with links to the {@link DiaryEntry} that caused
 * that encounter.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class EncounterRepresentationModelProcessor implements RepresentationModelProcessor<EncounterDto> {

	private final DiaryManagement diaries;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.RepresentationModelProcessor#process(org.springframework.hateoas.RepresentationModel)
	 */
	@Override
	public EncounterDto process(EncounterDto model) {

		var person = model.getPerson();
		var diary = diaries.findDiaryFor(person);
		var diaryController = on(DiaryController.class);

		diary.getEntryFor(model.getEncounter()).ifPresent(it -> {
			var handlerMethod = diaryController.getDiaryEntry(it.getId(), person);
			model.add(Link.of(fromMethodCall(handlerMethod).toUriString(), "diaryEntry"));
		});

		return model;
	}
}
