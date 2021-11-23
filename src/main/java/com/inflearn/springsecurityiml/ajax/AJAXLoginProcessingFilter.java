package com.inflearn.springsecurityiml.ajax;

import static org.thymeleaf.util.StringUtils.isEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.springsecurityiml.domain.AccountDto;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class AJAXLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String X_REQUESTED_WITH = "X-Requested-With";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AJAXLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException {
        if (isNotAjax(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (isEmpty(accountDto.getUsername()) || isEmpty(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(
            accountDto.getUsername(), accountDto.getPassword());

        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    private boolean isNotAjax(HttpServletRequest request) {
        return !(XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH)));
    }
}
