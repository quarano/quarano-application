package quarano.reference;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;

public interface SymptomRepository extends PagingAndSortingRepository<Symptom, UUID> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	Streamable<Symptom> findAll();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
	Streamable<Symptom> findAll(Sort sort);

	List<Symptom> findByName(String name);
}
