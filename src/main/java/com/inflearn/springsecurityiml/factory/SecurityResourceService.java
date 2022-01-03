package com.inflearn.springsecurityiml.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

@Service
public class SecurityResourceService {

    private final ResourceRepository resourceRepository;

    public SecurityResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        resourceRepository.findAll()
            .forEach(re-> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                    result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
                });
            });
        return result;
    }
}
