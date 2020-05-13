package quarano.department;

/**
 * @author Oliver Drotbohm
 */
class CompletedInitialReport extends InitialReport {

	/*
	 * (non-Javadoc)
	 * @see quarano.department.InitialReport#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return true;
	}
}
