package de.wevsvirushackathon.coronareport.authentication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {

    Optional<Account> findOneByUsername(String username);
}
