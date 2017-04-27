package com.mad2man.sbweb.filter;

import com.mad2man.sbweb.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public SimpleCORSFilter(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", String.valueOf(applicationProperties.getCors().getAllowCredentials()));
        response.setHeader("Access-Control-Allow-Methods", String.join(", ", applicationProperties.getCors().getAllowedMethods()));
        response.setHeader("Access-Control-Max-Age", String.valueOf(applicationProperties.getCors().getMaxAge()));
        response.setHeader("Access-Control-Allow-Headers", String.join(", ", applicationProperties.getCors().getAllowedHeaders()));

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
