package com.inflearn.springsecurityiml.service;

import com.inflearn.springsecurityiml.domain.Roles;
import java.util.List;

public interface RolesService {

    Roles getRole(long id);

    List<Roles> getRoles();

    void createRole(Roles role);
}
