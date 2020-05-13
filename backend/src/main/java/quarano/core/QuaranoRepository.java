package quarano.core;

import org.jddd.core.types.Identifier;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@NoRepositoryBean
public interface QuaranoRepository<T extends QuaranoAggregate<T, ID>, ID extends Identifier>
		extends PagingAndSortingRepository<T, ID> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	Streamable<T> findAll();
}
