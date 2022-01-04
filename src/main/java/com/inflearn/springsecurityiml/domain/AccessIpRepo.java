package com.inflearn.springsecurityiml.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepo extends JpaRepository<AccessIP, Long> {

    AccessIP findByIpAddress(String IpAdress);

}
