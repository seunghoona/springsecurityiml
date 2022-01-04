package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.domain.RolesHierarchy;
import com.inflearn.springsecurityiml.domain.RolesHierarchyRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RolesHierarchyServiceImpl implements RolesHierarchyService {

    private final RolesHierarchyRepo rolesHierarchyRepo;

    @Override
    public String createRoleHierarchy() {
        List<RolesHierarchy> all = rolesHierarchyRepo.findAll();

        StringBuilder stringBuilder = new StringBuilder();
        for (RolesHierarchy rolesHierarchy : all) {

            if(rolesHierarchy.getParentName() != null) {
                stringBuilder.append(rolesHierarchy.getParentName().getChildName());
                stringBuilder.append(" > ");
                stringBuilder.append(rolesHierarchy.getChildName());
                stringBuilder.append(" > ");
            }
        }

        return stringBuilder.toString();
    }
}
