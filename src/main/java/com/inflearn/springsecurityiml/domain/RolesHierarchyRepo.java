package com.inflearn.springsecurityiml.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesHierarchyRepo extends JpaRepository<RolesHierarchy, Long> {

    RolesHierarchy findByChildName(String roleName);

}
