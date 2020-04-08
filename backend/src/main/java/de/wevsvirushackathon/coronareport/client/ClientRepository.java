package de.wevsvirushackathon.coronareport.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository
        extends CrudRepository<Client, Long> {

    Client findByClientCode(String clientCode);

    List<Client> findAllByHealthDepartmentId(String healtDepartmentId);
    
    @Query("SELECT client FROM Account as account INNER JOIN account.client as client WHERE account.username = :username")
    public Optional<Client> findClientByAccountName( @Param("username") String username);
    

}