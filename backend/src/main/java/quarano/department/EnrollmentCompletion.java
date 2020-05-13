package quarano.department;

import quarano.tracking.Encounters;

/**
 * @author Oliver Drotbohm
 */
public enum EnrollmentCompletion {

	WITH_ENCOUNTERS {

		/*
		 * (non-Javadoc)
		 * @see quarano.department.EnrollmentCompletion#verify(quarano.tracking.Encounters)
		 */
		@Override
		boolean verify(Encounters encounters) {
			return encounters.hasAtLeastOneEncounter();
		}
	},

	WITHOUT_ENCOUNTERS {

		/*
		 * (non-Javadoc)
		 * @see quarano.department.EnrollmentCompletion#verify(quarano.tracking.Encounters)
		 */
		@Override
		boolean verify(Encounters encounters) {
			return true;
		}
	};

	abstract boolean verify(Encounters encounters);
}
