package de.wevsvirushackathon.coronareport.authentication.provider;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.wevsvirushackathon.coronareport.authentication.Account;

public interface AccountRepository extends CrudRepository<Account, String> {

    Optional<Account> findOneByUsername(String username);
}
