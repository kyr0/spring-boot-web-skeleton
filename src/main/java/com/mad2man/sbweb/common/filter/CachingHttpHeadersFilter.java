package com.mad2man.sbweb.common.filter;

import com.mad2man.sbweb.config.WebConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CachingHttpHeadersFilter implements Filter {

    private static final long LAST_MODIFIED = System.currentTimeMillis();
    private long cacheTimeToLive;

    private final WebConfig webConfig;

    public CachingHttpHeadersFilter(WebConfig webConfig) {

        this.cacheTimeToLive = TimeUnit.DAYS.toMillis(1461L);
        this.webConfig = webConfig;
    }

    public void init(FilterConfig filterConfig) throws ServletException {

        this.cacheTimeToLive = TimeUnit.DAYS.toMillis(
            webConfig.getCacheTimeToLiveInDays()
        );
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setHeader("Cache-Control", "max-age=" + this.cacheTimeToLive + ", public");
        httpResponse.setHeader("Pragma", "cache");
        httpResponse.setDateHeader("Expires", this.cacheTimeToLive + System.currentTimeMillis());
        httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);
        chain.doFilter(request, response);
    }
}
