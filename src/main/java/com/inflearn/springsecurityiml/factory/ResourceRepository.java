package com.inflearn.springsecurityiml.factory;

import com.inflearn.springsecurityiml.domain.Resources;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourceRepository extends JpaRepository<Resources, Long> {
    Resources findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

    @Query("select r from Resources r join fetch r.roleSet where r.resourceType = 'url' order by r.orderNum desc ")
    List<Resources> findAll();

    @Query("select r from Resources r join fetch r.roleSet where r.resourceType = 'method' order by r.orderNum desc ")
    List<Resources> findAllMethodResources();
}
