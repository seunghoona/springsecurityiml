package com.inflearn.springsecurityiml.config;

import com.inflearn.springsecurityiml.ajax.AJAXLoginProcessingFilter;
import com.inflearn.springsecurityiml.ajax.provider.AjaxAuthenticationProvider;
import com.inflearn.springsecurityiml.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(0)
@Configuration
@EnableWebSecurity
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .authorizeRequests()
            .anyRequest().authenticated();

        http.csrf().disable();

        http.addFilterBefore(ajaxLoginProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AJAXLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AJAXLoginProcessingFilter ajaxLoginProcessingFilter = new AJAXLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider();
    }
}
