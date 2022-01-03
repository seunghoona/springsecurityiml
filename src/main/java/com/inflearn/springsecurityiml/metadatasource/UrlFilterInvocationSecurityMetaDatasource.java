package com.inflearn.springsecurityiml.metadatasource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class UrlFilterInvocationSecurityMetaDatasource implements
    FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    public UrlFilterInvocationSecurityMetaDatasource(
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap) {
        this.requestMap = requestMap;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
        throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        if (requestMap != null) {
            return requestMap.entrySet()
                .stream()
                .filter(s -> s.getKey().matches(request))
                .map(Entry::getValue)
                .findFirst()
                .orElse(null);
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return requestMap.entrySet()
            .stream()
            .flatMap(s -> s.getValue().stream())
            .collect(Collectors.toSet());

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
