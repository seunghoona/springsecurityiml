package com.inflearn.springsecurityiml.factory;

import com.inflearn.springsecurityiml.domain.AccessIP;
import com.inflearn.springsecurityiml.domain.AccessIpRepo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

@Service
public class SecurityResourceService {

    private final ResourceRepository resourceRepository;
    private final AccessIpRepo accessIpRepo;

    public SecurityResourceService(ResourceRepository resourceRepository,
        AccessIpRepo accessIpRepo) {
        this.resourceRepository = resourceRepository;
        this.accessIpRepo = accessIpRepo;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        resourceRepository.findAll()
            .forEach(re-> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                });
                result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
            });
        return result;
    }

    public LinkedHashMap<String, List<ConfigAttribute>> getMethodResourceList() {
        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        resourceRepository.findAllMethodResources()
            .forEach(re-> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                    result.put(re.getResourceName(), configAttributes);
                });
            });
        return result;
    }

    public List<String> getAccessIp() {
        return accessIpRepo.findAll()
            .stream().map(AccessIP::getIpAddress).collect(Collectors.toList());
    }
}
