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

import de.wevsvirushackathon.coronareport.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import quarano.tracking.Address.HouseNumber;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Order(600)
@Component
@RequiredArgsConstructor
public class TrackedPersonDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final TrackedPersonRepository trackedPeople;
	private final ClientRepository clients;
	private final ModelMapper mapper;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		Streamable.of(clients.findAll()).forEach(it -> {

			var person = mapper.map(it, TrackedPerson.class);
			var zipCode = it.getZipCode() == null ? null : ZipCode.of(it.getZipCode());
			var address = new Address(it.getStreet(), HouseNumber.NONE, null, zipCode);

			trackedPeople.save(person.setAddress(address));
		});
	}
}
