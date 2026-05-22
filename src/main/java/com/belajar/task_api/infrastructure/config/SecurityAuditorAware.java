package com.belajar.task_api.infrastructure.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Cek apakah ada session/user yang sedang login
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM"); // Fallback jika dijalankan tanpa login (misal background job/system startup)
        }

        // Mengembalikan username yang diekstrak dari JWT token Anda
        return Optional.of(authentication.getName());
    }
}