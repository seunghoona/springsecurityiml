package com.inflearn.springsecurityiml.listener;

import com.inflearn.springsecurityiml.domain.AccessIP;
import com.inflearn.springsecurityiml.domain.AccessIpRepo;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.Password;
import com.inflearn.springsecurityiml.domain.Resources;
import com.inflearn.springsecurityiml.domain.Roles;
import com.inflearn.springsecurityiml.domain.RolesHierarchy;
import com.inflearn.springsecurityiml.domain.RolesHierarchyRepo;
import com.inflearn.springsecurityiml.domain.RolesRepo;
import com.inflearn.springsecurityiml.factory.ResourceRepository;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepo roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RolesHierarchyRepo roleHierarchyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessIpRepo accessIpRepository;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
        setupAccessIpData();

        alreadySetup = true;
    }


    private void setupSecurityResources() {

        Set<Roles> roles = new HashSet<>();
        Roles adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);

        createUserIfNotFound("admin", "pass", "admin@gmail.com", 10, roles);

        Set<Roles> roles1 = new HashSet<>();
        Roles managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        roles1.add(managerRole);

        createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);
        createRoleHierarchyIfNotFound(managerRole, adminRole);

        Set<Roles> roles3 = new HashSet<>();
        Roles childRole1 = createRoleIfNotFound("ROLE_USER", "회원");
        roles3.add(childRole1);

        createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);
        createRoleHierarchyIfNotFound(childRole1, managerRole);

        createResourceIfNotFound("/admin/**", "", roles, "url");

    }

    @Transactional
    public Roles createRoleIfNotFound(String roleName, String roleDesc) {

        Roles role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Roles.builder()
                .roleName(roleName)
                .roleDesc(roleDesc)
                .build();
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String email, int age,
        Set<Roles> roleSet) {

        Account account = userRepository.findByUsername(userName).orElse(null);

        if (account == null) {
            account = Account.builder()
                .username(userName)
                .email(email)
                .age(age)
                .password(new Password(passwordEncoder.encode(password)))
                .userRoles(roleSet)
                .build();
        }
        return userRepository.save(account);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Roles childRole, Roles parentRole) {

        RolesHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(
            parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RolesHierarchy.builder()
                .childName(parentRole.getRoleName())
                .build();
        }
        RolesHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RolesHierarchy.builder()
                .childName(childRole.getRoleName())
                .build();
        }

        RolesHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Roles> roleSet, String resourceType) {
        Resources resources = resourceRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                .resourceName(resourceName)
                .roleSet(roleSet)
                .httpMethod(httpMethod)
                .resourceType(resourceType)
                .orderNum(count.incrementAndGet())
                .build();
        }
        return resourceRepository.save(resources);
    }

    private void setupAccessIpData() {
        AccessIP byIpAddress = accessIpRepository.findByIpAddress("127.0.0.1");
        if (byIpAddress == null) {
            AccessIP accessIp = AccessIP.builder()
                .ipAddress("127.0.0.1")
                .build();
            accessIpRepository.save(accessIp);
        }

    }
}
