package com.inflearn.springsecurityiml.ajax.provider;

import com.inflearn.springsecurityiml.ajax.AjaxAuthenticationToken;
import com.inflearn.springsecurityiml.domain.AccountContext;
import com.inflearn.springsecurityiml.application.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AjaxAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String name = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(
            name);
        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("인증실패");
        }

        return new AjaxAuthenticationToken(accountContext, null, accountContext.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 토큰 UsernamePasswordAuthenticationToken 같을 때 검증
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
