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
