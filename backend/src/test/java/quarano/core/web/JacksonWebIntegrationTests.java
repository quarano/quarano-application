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
package quarano.core.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class JacksonWebIntegrationTests {

	private final ObjectMapper jackson;

	@Test
	void turnsEmptyStringIntoNull() throws Exception {
		assertThat(jackson.readValue("{ \"name\" : \"\" }", Sample.class).name).isNull();
		assertThat(jackson.readValue("{ \"name\" : \" \" }", Sample.class).name).isNull();
	}

	@Test
	void trimsStringValuesOnBind() throws Exception {
		assertThat(jackson.readValue("{ \"name\" : \" foo bar \" }", Sample.class).name).isEqualTo("foo bar");
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	static class Sample {
		String name;
	}
}
