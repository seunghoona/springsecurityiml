package com.inflearn.springsecurityiml.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class FormWebAuthenctionDetails extends WebAuthenticationDetails {

    private String secretKey;

    public FormWebAuthenctionDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");

    }

    public String getSecretKey() {
        return secretKey;
    }
}
