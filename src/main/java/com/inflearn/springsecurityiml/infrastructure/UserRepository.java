package com.inflearn.springsecurityiml.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inflearn.springsecurityiml.domain.Account;

public interface UserRepository extends JpaRepository<Account, Long> {

	Optional<Account>  findByUsername(String username);
}
