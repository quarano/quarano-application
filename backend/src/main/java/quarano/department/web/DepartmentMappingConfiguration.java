package quarano.department.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.MappingCustomizer;
import quarano.department.TestResult;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseProperties;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.tracking.Quarantine;

import java.util.stream.Stream;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Oliver Drotbohm
 */
@Component
@Order(30)
@RequiredArgsConstructor
public class DepartmentMappingConfiguration implements MappingCustomizer {

	private final @NonNull TrackedCaseProperties caseProperties;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void customize(ModelMapper mapper) {

		Stream.of(TrackedCaseDto.Input.class, TrackedCaseDto.Output.class).forEach(type -> {

			mapper.typeMap(TrackedCase.class, (Class<TrackedCaseDto>) type)
					.setPreConverter(QuarantineMappingConverter.INSTANCE)
					.addMappings(it -> {
						it.skip(TrackedCaseDto::setQuarantineStartDate);
						it.skip(TrackedCaseDto::setQuarantineEndDate);
					});
		});

		mapper.typeMap(TrackedCaseDto.Input.class, TrackedCase.class).setPreConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();

			return target.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate()));

		}).setPostConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();
			var testDate = source.getTestDate();

			if (source.getTestDate() != null) {
				target.report(source.isInfected() ? TestResult.infected(testDate) : TestResult.notInfected(testDate),
						caseProperties.isExecuteContactRetroForContactCases());
			}

			return target;
		});
	}

	private static enum QuarantineMappingConverter implements Converter<TrackedCase, TrackedCaseDto> {

		INSTANCE;

		/*
		 * (non-Javadoc)
		 * @see org.modelmapper.Converter#convert(org.modelmapper.spi.MappingContext)
		 */
		@Override
		@SuppressWarnings("null")
		public TrackedCaseDto convert(MappingContext<TrackedCase, TrackedCaseDto> it) {

			var source = it.getSource();
			var target = it.getDestination();
			var quarantine = source.getQuarantine();

			if (quarantine != null) {
				target.setQuarantineStartDate(quarantine.getFrom())
						.setQuarantineEndDate(quarantine.getTo());
			}

			if (source.hasTestResult()) {

				var result = source.getTestResult();

				target.setInfected(result.isInfected())
						.setTestDate(result.getTestDate());
			}

			return target;
		}
	}
}
