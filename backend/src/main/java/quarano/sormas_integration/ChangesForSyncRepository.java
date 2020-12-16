package quarano.sormas_integration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ChangesForSyncRepository extends CrudRepository<ChangesForSync, Integer> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	@Override
	Streamable<ChangesForSync> findAll();
}
