package com.inflearn.springsecurityiml.service;

import com.inflearn.springsecurityiml.domain.Roles;
import com.inflearn.springsecurityiml.domain.RolesRepo;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RolesService {

    private final RolesRepo roleRepository;

    @Transactional
    public Roles getRole(long id) {
        return roleRepository.findById(id).orElse(new Roles());
    }

    @Transactional
    public List<Roles> getRoles() {

        return roleRepository.findAll();
    }

    @Transactional
    public void createRole(Roles role) {

        roleRepository.save(role);
    }

}
