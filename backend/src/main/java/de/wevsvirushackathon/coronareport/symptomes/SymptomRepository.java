package de.wevsvirushackathon.coronareport.symptomes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomRepository
        extends CrudRepository<Symptom, Long> {

    @Query("SELECT s FROM Symptom s WHERE s.name = :name")
    List<Symptom> findAllByName(@Param("name") String name);
}