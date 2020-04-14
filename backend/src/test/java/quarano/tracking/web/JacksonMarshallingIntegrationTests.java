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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import de.wevsvirushackathon.coronareport.CoronareportBackendApplication;
import quarano.Quarano;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@SpringBootTest(classes = { Quarano.class, CoronareportBackendApplication.class })
@ActiveProfiles("integrationtest")
public class JacksonMarshallingIntegrationTests {

	@Autowired ObjectMapper mapper;

	@Test
	@Disabled
	void rendersIdentifierForContactPersonDtoButDoesNotBindIt() throws Exception {

		var dto = new ContactPersonDto();
		dto.setId(ContactPersonIdentifier.of(UUID.randomUUID()));

		var result = mapper.writeValueAsString(dto);

		assertThat(JsonPath.parse(result).read("$.id", String.class)).isNotEmpty();

		dto = mapper.readValue(result, ContactPersonDto.class);

		assertThat(dto.getId()).isNull();
	}
}
