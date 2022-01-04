package com.inflearn.springsecurityiml.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Roles, Long> {

    Roles findByRoleName(String roleName);

}
