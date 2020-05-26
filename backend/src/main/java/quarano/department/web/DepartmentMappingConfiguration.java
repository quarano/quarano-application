package quarano.department.web;

import quarano.core.web.MappingCustomizer;
import quarano.department.TestResult;
import quarano.department.TrackedCase;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.tracking.Quarantine;

import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Oliver Drotbohm
 */
@Component
@Order(30)
public class DepartmentMappingConfiguration implements MappingCustomizer {

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

		// TrackedCase

		mapper.typeMap(TrackedCase.class, TrackedCaseDto.class).setPreConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();
			var quarantine = source.getQuarantine();

			if (quarantine != null) {
				target.setQuarantineStartDate(quarantine.getFrom()) //
						.setQuarantineEndDate(quarantine.getTo());
			}

			if (source.hasTestResult()) {

				var result = source.getTestResult();

				target.setInfected(result.isInfected()) //
						.setTestDate(result.getTestDate());
			}

			return target;

		}).addMappings(it -> {
			it.skip(TrackedCaseDto::setQuarantineStartDate);
			it.skip(TrackedCaseDto::setQuarantineEndDate);
		});

		mapper.typeMap(TrackedCaseDto.class, TrackedCase.class).setPreConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();

			return target.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate()));

		}).setPostConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();
			var testDate = source.getTestDate();

			if (source.getTestDate() != null) {
				target.report(source.isInfected() ? TestResult.infected(testDate) : TestResult.notInfected(testDate));
			}

			return target;
		});
	}
}
