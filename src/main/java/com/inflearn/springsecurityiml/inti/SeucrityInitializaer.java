package com.inflearn.springsecurityiml.inti;

import com.inflearn.springsecurityiml.application.service.RolesHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@RequiredArgsConstructor
public class SeucrityInitializaer implements ApplicationRunner {

    private final RolesHierarchyService rolesHierarchyService;
    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String roleHierarchy = rolesHierarchyService.createRoleHierarchy();
        this.roleHierarchy.setHierarchy(roleHierarchy);
    }
}
