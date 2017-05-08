package com.mad2man.sbweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mad2man.sbweb.auth.CustomAuthenticationProvider;
import com.mad2man.sbweb.auth.RestAuthenticationEntryPoint;
import com.mad2man.sbweb.auth.SkipPathRequestMatcher;
import com.mad2man.sbweb.auth.filter.LoginProcessingFilter;
import com.mad2man.sbweb.auth.token.JwtAuthenticationProvider;
import com.mad2man.sbweb.auth.token.extractor.TokenExtractor;
import com.mad2man.sbweb.auth.token.filter.JwtTokenAuthenticationProcessingFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String TOKEN_BASED_AUTH_ENDPOINTS = "/api/**";

    @Autowired private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AuthenticationSuccessHandler successHandler;
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ObjectMapper objectMapper;

    protected LoginProcessingFilter buildLoginProcessingFilter() throws Exception {

        LoginProcessingFilter filter = new LoginProcessingFilter(
            LOGIN_ENDPOINT, successHandler, failureHandler, objectMapper
        );

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {

        // TODO: To be enhanced by: Forgot username, forgot password, etc.
        List<String> pathsToSkip = Arrays.asList(LOGIN_ENDPOINT);

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

            // We don't need CSRF for JWT based authentication, as long as it's not Set-Cookie based.
            .csrf().disable()
            .exceptionHandling()

            // provide WebSecurity with UserContext and Authorities
            .authenticationEntryPoint(this.authenticationEntryPoint)

            // only request-based session object
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // allow swagger API docs to be viewed by everyone
            .and()
                .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/", "/libs/**", "/img/**", "/*.html").permitAll()

            // protect all other endpoints
            .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENDPOINTS).authenticated()

            .and()

                // login + token issuing
                .addFilterBefore(buildLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)

                // pure token based login
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
