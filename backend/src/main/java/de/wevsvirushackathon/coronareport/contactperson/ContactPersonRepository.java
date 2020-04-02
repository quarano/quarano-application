package de.wevsvirushackathon.coronareport.contactperson;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository
        extends CrudRepository<ContactPerson, Long> {

    @Query("SELECT c FROM ContactPerson c WHERE c.client.clientCode = :clientCode")
    Iterable<ContactPerson> findAllByClientCode(@Param("clientCode") String clientCode);
}