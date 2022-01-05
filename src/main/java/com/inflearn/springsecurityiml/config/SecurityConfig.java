package com.inflearn.springsecurityiml.config;

import com.inflearn.springsecurityiml.common.FormAuthenticationDetailsSource;
import com.inflearn.springsecurityiml.factory.PermitAllFilter;
import com.inflearn.springsecurityiml.factory.SecurityResourceService;
import com.inflearn.springsecurityiml.factory.UrlResourceMapFactoryBean;
import com.inflearn.springsecurityiml.handler.CustomAcessDeniedHandler;
import com.inflearn.springsecurityiml.metadatasource.UrlFilterInvocationSecurityMetaDatasource;
import com.inflearn.springsecurityiml.provider.CustomAuthenticationProvider;
import com.inflearn.springsecurityiml.voter.CustomRoleHierarchyVoter;
import com.inflearn.springsecurityiml.voter.GroupVoter;
import com.inflearn.springsecurityiml.voter.IpAddressVoter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
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

    private static final String[] PERMIT_ALL_URL = {"/", "login", "/user/login/**"};

    private final SecurityResourceService securityResourceService;

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
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
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
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {

        PermitAllFilter filterSecurityInterceptor = new PermitAllFilter(PERMIT_ALL_URL);
        filterSecurityInterceptor.setSecurityMetadataSource(
            urlFilterInvocationSecurityMetaDatasource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());

        return filterSecurityInterceptor;
    }

    @Bean
    public UrlFilterInvocationSecurityMetaDatasource urlFilterInvocationSecurityMetaDatasource()
        throws Exception {
        return new UrlFilterInvocationSecurityMetaDatasource(
            urlResourceMapFactoryBean().getObject(), securityResourceService);
    }

    @Bean
    public UrlResourceMapFactoryBean urlResourceMapFactoryBean() {
        return new UrlResourceMapFactoryBean(securityResourceService);
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {

        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();

        accessDecisionVoters.add(accessIp());
        accessDecisionVoters.add(roleVoter());
        accessDecisionVoters.add(groupVoter());

        return accessDecisionVoters;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> accessIp() {
        return new IpAddressVoter(securityResourceService);
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new CustomRoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return new RoleHierarchyImpl();
    }

    @Bean
    public AccessDecisionVoter<? extends Object> groupVoter() {
        return new GroupVoter(roleGroupHierarchy());
    }

    @Bean
    public RoleHierarchy roleGroupHierarchy() {
        return new RoleHierarchyImpl();
    }

}
