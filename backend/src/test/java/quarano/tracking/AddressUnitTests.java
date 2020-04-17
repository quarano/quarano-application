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

import static org.assertj.core.api.Assertions.*;

import quarano.tracking.Address.HouseNumber;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class AddressUnitTests {

	@Test
	void createsNullHouseNumberObjectForNullSource() {
		assertThat(HouseNumber.of(null)).isEqualTo(HouseNumber.NONE);
	}

	@Test
	void createsSimpleHouseNumber() {
		assertThat(HouseNumber.of("4711").toString()).isEqualTo("4711");
	}

	@Test
	void considersAddressWithoutHouseNumberComplete() {

		Address address = new Address("Steet", HouseNumber.NONE, "City", ZipCode.of("68199"));

		assertThat(address.isComplete()).isTrue();
	}
}
