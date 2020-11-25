package quarano.occasion;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.DataInitializer;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

/**
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Component
@RequiredArgsConstructor
public class OccasionDataInitializer implements DataInitializer {

	public final static OccasionCode OCCASION_CODE_1 = OccasionCode.of("EVENTNUM");

	private final @NonNull OccasionRepository occasions;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		occasions.save(new Occasion("Sample event", LocalDate.now().atStartOfDay(),
				LocalDate.now().plusDays(1).atStartOfDay(), OCCASION_CODE_1));
	}
}
