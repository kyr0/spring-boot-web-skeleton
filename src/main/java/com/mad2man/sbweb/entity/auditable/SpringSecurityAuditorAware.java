package com.mad2man.sbweb.entity.auditable;

import com.mad2man.sbweb.auth.model.UserContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security based on user id.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    // user id
    @Override
    public String getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((UserContext) authentication.getPrincipal()).getUserId();
    }
}
