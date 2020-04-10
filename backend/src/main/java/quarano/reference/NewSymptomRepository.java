package quarano.reference;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewSymptomRepository extends CrudRepository<NewSymptom, Long> {

	@Query("SELECT s FROM Symptom s WHERE s.name = :name")
	List<NewSymptom> findAllByName(@Param("name") String name);
}
