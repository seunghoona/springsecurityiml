package com.inflearn.springsecurityiml.voter;

import com.inflearn.springsecurityiml.factory.SecurityResourceService;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RequiredArgsConstructor
@Slf4j
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService resourceService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object,
        Collection<ConfigAttribute> attributes) {

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();

        String remoteAddress = details.getRemoteAddress();

        List<String> accessIps = resourceService.getAccessIp();

        log.debug("IP 정보 : {}", remoteAddress);

        for (String accessIp : accessIps) {
            if (accessIp.equals(remoteAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        throw new AccessDeniedException("잘못된 IP 정보입니다.");
    }
}
