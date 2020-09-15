package quarano.health_departments;

import static org.assertj.core.api.Assertions.*;

import quarano.health_departments.HealthDepartmentOverview.HealthDepartment;
import quarano.health_departments.HealthDepartmentOverview.HealthDepartment.Address;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

public class HealthDepartmentOverviewTest {

	@Test
	void testGetAllHealthDepartments() {

		var overview = new HealthDepartmentOverview();

		var healthDepartments = overview.getAllHealthDepartments();

		assertThat(healthDepartments).hasSize(399);

		var first = assertThat(healthDepartments).first();
		first.extracting(HealthDepartment::getName).isEqualTo("Stadt Flensburg");
		first.extracting(HealthDepartment::getCode).isEqualTo("1.01.0.01.");
		first.extracting(HealthDepartment::getDepartment).isEqualTo("Fachbereich 2 / Gesundheitsdienste");
		first.extracting(HealthDepartment::getPhone).asString().isEqualTo("0461852602");
		first.extracting(HealthDepartment::getFax).asString().isEqualTo("0461851960");
		first.extracting(HealthDepartment::getEmail).asString().isEqualTo("amtsarzt@flensburg.de");

		Address address = healthDepartments.get(0).getAddress();
		assertThat(address.getStreet()).isEqualTo("Norderstr. 58-60");
		assertThat(address.getZipcode()).asString().isEqualTo("24939");
		assertThat(address.getPlace()).isEqualTo("Flensburg");

		var searchStrings = first.extracting(HealthDepartment::getSearchTexts).asList();
		searchStrings.first().isEqualTo("24937");
		searchStrings.last().isEqualTo("Westliche Höhe");
	}

	@Test
	void testFindHealthDepartmentWithExact() {

		var overview = new HealthDepartmentOverview();

		var healthDepartment = overview.findHealthDepartmentWithExact("01665");

		assertThat(healthDepartment).isPresent();

		checkDepartmentMeissen(assertThat(healthDepartment.get()));
	}

	@Test
	void testCantFindHealthDepartmentWithExact() {

		var overview = new HealthDepartmentOverview();

		var healthDepartment = overview.findHealthDepartmentWithExact("99999");

		assertThat(healthDepartment).isEmpty();
	}

	@Test
	void testFindHealthDepartmentContains() {

		var overview = new HealthDepartmentOverview();

		var healthDepartments = overview.findHealthDepartmentsContains("1665");

		assertThat(healthDepartments).hasSize(2);

		checkDepartmentMeissen(assertThat(healthDepartments).last());
	}

	@Test
	void testCantFindHealthDepartmentContains() {

		var overview = new HealthDepartmentOverview();

		var healthDepartments = overview.findHealthDepartmentsContains("ABC");

		assertThat(healthDepartments).isEmpty();
	}

	private void checkDepartmentMeissen(ObjectAssert<HealthDepartment> department) {
		department.extracting(HealthDepartment::getName).isEqualTo("Landkreis Meißen");
		department.extracting(HealthDepartment::getCode).isEqualTo("1.14.6.27.01.");
		department.extracting(HealthDepartment::getDepartment).isEqualTo("Gesundheitsamt");
		department.extracting(HealthDepartment::getPhone).asString().isEqualTo("035217253455");
		department.extracting(HealthDepartment::getFax).asString().isEqualTo("0352172588054");
		department.extracting(HealthDepartment::getEmail).asString().isEqualTo("ga.infektionsschutz@kreis-meissen.de");

		var address = department.extracting(HealthDepartment::getAddress);
		address.extracting(Address::getStreet).isEqualTo("Dresdner Str. 25");
		address.extracting(Address::getZipcode).asString().isEqualTo("01662");
		address.extracting(Address::getPlace).isEqualTo("Meißen");

		var searchStrings = department.extracting(HealthDepartment::getSearchTexts).asList();
		searchStrings.first().isEqualTo("01445");
		searchStrings.last().isEqualTo("Zschorna");
	}
}
