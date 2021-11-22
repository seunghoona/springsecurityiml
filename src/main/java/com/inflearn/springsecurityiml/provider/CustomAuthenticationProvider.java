package com.inflearn.springsecurityiml.provider;

import com.inflearn.springsecurityiml.common.FormAuthenticationDetailsSource;
import com.inflearn.springsecurityiml.common.FormWebAuthenctionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inflearn.springsecurityiml.domain.AccountContext;
import com.inflearn.springsecurityiml.service.UserDetailsService;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = (String)authentication.getCredentials();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(name);
        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("인증실패");
        }
        secretKey(authentication);

        return new UsernamePasswordAuthenticationToken(accountContext,null, accountContext.getAuthorities());
    }

    private void secretKey(Authentication authentication) {
        FormWebAuthenctionDetails details = (FormWebAuthenctionDetails) authentication.getDetails();
        String secretKey = details.getSecretKey();
        if (!"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("시크릿키가 존재하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 토큰 UsernamePasswordAuthenticationToken 같을 때 검증
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
