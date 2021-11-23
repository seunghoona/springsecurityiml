package com.inflearn.springsecurityiml.config;

import com.inflearn.springsecurityiml.ajax.AJAXLoginProcessingFilter;
import com.inflearn.springsecurityiml.common.FormAuthenticationDetailsSource;
import com.inflearn.springsecurityiml.handler.CustomAcessDeniedHandler;
import com.inflearn.springsecurityiml.provider.CustomAuthenticationProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {
        return new FormAuthenticationDetailsSource();
    }

    @Override

    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/mypage").hasRole("USER")
            .antMatchers("/messages").hasRole("MANAGER")
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/doLogin")
            .defaultSuccessUrl("/")
            .authenticationDetailsSource(authenticationDetailsSource())
            .failureHandler(failureHandler())
            .permitAll();
        http.csrf().disable();

        http.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());

        http.addFilterBefore(ajaxLoginProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);

    }

    public AccessDeniedHandler accessDeniedHandler() {
        CustomAcessDeniedHandler customAcessDeniedHandler = new CustomAcessDeniedHandler();
        customAcessDeniedHandler.setErrorPage("/denied");
        return customAcessDeniedHandler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new com.inflearn.springsecurityiml.handler.AuthenticationFailureHandler();
    }

    @Bean
    public AJAXLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AJAXLoginProcessingFilter ajaxLoginProcessingFilter = new AJAXLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
