package com.inflearn.springsecurityiml.inti;

import com.inflearn.springsecurityiml.application.service.RolesHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityInitializer implements ApplicationRunner {

    private final RolesHierarchyService rolesHierarchyService;
    private final RoleHierarchyImpl roleHierarchy;

    @Qualifier("roleGroupHierarchy")
    private final RoleHierarchyImpl roleGroupHierarchy;

    @Override
    public void run(ApplicationArguments args) {
        String roleHierarchy = rolesHierarchyService.createRoleHierarchy();
        this.roleHierarchy.setHierarchy(roleHierarchy);
        this.roleGroupHierarchy.setHierarchy("SCM > SM\nSM > TL\nTL > T");
    }
}
