package com.mad2man.sbweb.common.filter;

import com.mad2man.sbweb.config.CorsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private final CorsConfig corsConfig;

    @Autowired
    public SimpleCORSFilter(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // set the origin dynamically (request origin always allowed; e.g. abc.com -> localhost)
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", String.valueOf(corsConfig.getAllowCredentials()));
        response.setHeader("Access-Control-Allow-Methods", String.join(", ", corsConfig.getAllowedMethods()));
        response.setHeader("Access-Control-Max-Age", String.valueOf(corsConfig.getMaxAge()));
        response.setHeader("Access-Control-Allow-Headers", String.join(", ", corsConfig.getAllowedHeaders()));

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
