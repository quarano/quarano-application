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
package quarano.masterdata;

import io.vavr.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.EmailTemplates;
import quarano.core.I18nProperties;

import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Configuration class to verify the presence of static texts for frontend and emails available for all {@link Locale}s
 * configured for the application to support to be available.
 *
 * @author Oliver Drotbohm
 * @see I18nProperties#getSupportedLocales()
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
class MasterdataConfiguration {

	private final FrontendTextRepository frontendTexts;
	private final EmailTextRepository emailTexts;
	private final I18nProperties i18nProperties;

	@EventListener
	void on(ApplicationStartedEvent event) {

		var locales = i18nProperties.getSupportedLocales();

		locales.stream()
				.flatMap(it -> Stream.of(FrontendText.Keys.values()).map(key -> Tuple.of(key, it)))
				.forEach(it -> {
					if (it.apply(frontendTexts::findByTextKey).isEmpty()) {
						throw new IllegalStateException(String.format("Missing frontend text for %s!", it));
					}
				});

		log.info("Successfully verified frontend texts for locales {}.", locales);

		locales.stream()
				.flatMap(it -> Stream.of(EmailTemplates.Keys.values()).map(key -> Tuple.of(key, it)))
				.forEach(it -> {
					if (it.apply(emailTexts::findByTextKey).isEmpty()) {
						throw new IllegalStateException(String.format("Missing email text for %s!", it));
					}
				});

		log.info("Successfully verified email texts for locales {}.", locales);
	}
}
