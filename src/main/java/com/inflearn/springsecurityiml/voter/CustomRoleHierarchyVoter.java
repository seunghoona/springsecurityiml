package com.inflearn.springsecurityiml.voter;

import java.util.Collection;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomRoleHierarchyVoter extends RoleHierarchyVoter {

    public CustomRoleHierarchyVoter(RoleHierarchy roleHierarchy) {
        super(roleHierarchy);
    }

    @Override
    public int vote(Authentication authentication, Object object,
        Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_ABSTAIN;
                    }
                }
            }
        }
        return ACCESS_DENIED;
    }

    Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities();
    }
}
