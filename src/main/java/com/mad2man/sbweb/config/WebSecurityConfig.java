package com.mad2man.sbweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mad2man.sbweb.auth.CustomAuthenticationProvider;
import com.mad2man.sbweb.auth.RestAuthenticationEntryPoint;
import com.mad2man.sbweb.auth.SkipPathRequestMatcher;
import com.mad2man.sbweb.auth.filter.LoginProcessingFilter;
import com.mad2man.sbweb.auth.jwt.JwtAuthenticationProvider;
import com.mad2man.sbweb.auth.jwt.extractor.TokenExtractor;
import com.mad2man.sbweb.auth.jwt.filter.JwtTokenAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String TOKEN_BASED_AUTH_ENDPOINTS = "/api/**";
    public static final String TOKEN_REFRESH_ENDPOINT = "/api/auth/token";

    @Autowired private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AuthenticationSuccessHandler successHandler;
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ObjectMapper objectMapper;

    public WebSecurityConfig() {
    }

    public CustomAuthenticationProvider getCustomAuthenticationProvider() {
        return customAuthenticationProvider;
    }

    public JwtAuthenticationProvider getJwtAuthenticationProvider() {
        return jwtAuthenticationProvider;
    }

    public RestAuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    public AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public TokenExtractor getTokenExtractor() {
        return tokenExtractor;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    protected LoginProcessingFilter buildLoginProcessingFilter() throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(LOGIN_ENDPOINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {

        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENDPOINT, LOGIN_ENDPOINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENDPOINTS);

        JwtTokenAuthenticationProcessingFilter filter
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        auth.authenticationProvider(customAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable() // We don't need CSRF for JWT based authentication
            .exceptionHandling()
            .authenticationEntryPoint(this.authenticationEntryPoint)

            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
                .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/", "/libs/**", "/img/**", "/*.html").permitAll() // TODO!
                    .antMatchers(LOGIN_ENDPOINT).permitAll() // Login end-point
                    .antMatchers(TOKEN_REFRESH_ENDPOINT).permitAll() // Token refresh end-point

            .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENDPOINTS).authenticated() // Protected API end-points

            .and()
                .addFilterBefore(buildLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
