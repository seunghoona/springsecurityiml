package com.inflearn.springsecurityiml.config;

import com.inflearn.springsecurityiml.common.FormAuthenticationDetailsSource;
import com.inflearn.springsecurityiml.factory.SecurityResourceService;
import com.inflearn.springsecurityiml.factory.UrlResourceMapFactoryBean;
import com.inflearn.springsecurityiml.handler.CustomAcessDeniedHandler;
import com.inflearn.springsecurityiml.metadatasource.UrlFilterInvocationSecurityMetaDatasource;
import com.inflearn.springsecurityiml.provider.CustomAuthenticationProvider;
import java.sql.CallableStatement;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
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
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Order(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityResourceService securityResourceService;

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

        http.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());

        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
        ;


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
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {

        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetaDatasource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());

        return filterSecurityInterceptor;
    }


    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }

    @Bean
    public UrlFilterInvocationSecurityMetaDatasource urlFilterInvocationSecurityMetaDatasource()
        throws Exception {
        return new UrlFilterInvocationSecurityMetaDatasource(urlResourceMapFactoryBean().getObject());
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    @Bean
    public UrlResourceMapFactoryBean urlResourceMapFactoryBean() {
        return new UrlResourceMapFactoryBean(securityResourceService);
    }
}
